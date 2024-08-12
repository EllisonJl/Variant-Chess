// Wait for the DOM content to be fully loaded before executing the script
document.addEventListener("DOMContentLoaded", function() {
    // Get references to important HTML elements
    const chessboard = document.getElementById("chessboard"); // Chessboard container
    const ruleDisplay = document.getElementById("ruleDisplay"); // Display for the current rule
    const specificRuleDisplay = document.getElementById("specificRuleDisplay"); // Display for specific rule details
    const restartButton = document.getElementById("restartButton"); // Button to restart the game
    let isWhiteTurn = true; // Boolean to track whose turn it is (White starts first)
    const undoButton = document.getElementById("undoButton"); // Button to undo the last move
    const redoButton = document.getElementById("redoButton"); // Button to redo the last undone move

    // Add event listener for the Cannon Special Rule button
    document.getElementById("cannonExplodeBtn").addEventListener("click", function() {
        console.log("CannonSpecialRule button clicked");
        setGameRule("CannonSpecialRule"); // Set the game rule to Cannon Special Rule
    });

    // Add event listener for the King Queen Capture Rule button
    document.getElementById("kingQueenCaptureBtn").addEventListener("click", function() {
        console.log("KingQueenSpecialRule button clicked");
        setGameRule("KingQueenSpecialRule"); // Set the game rule to King Queen Capture Rule
    });

    // Add event listener for the Pawn Promotion Rule button
    document.getElementById("pawnPromotionBtn").addEventListener("click", function() {
        console.log("PawnPromotionRule button clicked");
        setGameRule("PawnPromotionRule"); // Set the game rule to Pawn Promotion Rule
    });

    // Function to set the game rule and reset the board
    function setGameRule(rule) {
        console.log(`Setting game rule to: ${rule}`); // Debugging log
        fetch(`/api/game/setRule/${rule}`, { // Send a POST request to set the game rule
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(() => {
            console.log("Game rule set successfully, resetting board."); // Debugging log
            clearBoard(); // Clear the board for a fresh start
            fetchInitialBoard(); // Fetch the initial board state
            fetchCurrentRule(); // Fetch and display the current rule
            isWhiteTurn = true; // Reset turn to White
            resetGameStatus(); // Reset the game status display
        }).catch(error => console.error("Error setting game rule:", error)); // Handle errors
    }

    // Function to reset the game status indicators
    function resetGameStatus() {
        const progressBar = document.getElementById('advantageBar'); // Progress bar element
        progressBar.style.width = "50%"; // Reset to the middle position

        const advantageText = document.getElementById('advantageColor'); // Text element for advantage
        advantageText.textContent = "Neither side has the advantage"; // Reset text
        document.getElementById('status').textContent = "Draw"; // Reset status text
    }

    // Add event listener for the undo button
    undoButton.addEventListener("click", function() {
        fetch("/api/game/undo", { // Send a POST request to undo the last move
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.text())
            .then(result => {
                if (result === "UNDO_SUCCESS") {
                    fetchUpdatedBoard(); // Fetch and display the updated board
                } else {
                    alert("No move to undo!"); // Alert if there is no move to undo
                }
            })
            .catch(error => console.error("Error undoing move:", error)); // Handle errors
    });

    // Add event listener for the redo button
    redoButton.addEventListener("click", function() {
        fetch("/api/game/redo", { // Send a POST request to redo the last undone move
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.text())
            .then(result => {
                if (result === "REDO_SUCCESS") {
                    fetchUpdatedBoard(); // Fetch and display the updated board
                } else {
                    alert("No move to redo!"); // Alert if there is no move to redo
                }
            })
            .catch(error => console.error("Error redoing move:", error)); // Handle errors
    });

    // Function to fetch and display the initial board state
    function fetchInitialBoard() {
        fetch("/api/game/initialBoard")
            .then(response => response.json())
            .then(board => {
                console.log("Fetched initial board:", JSON.stringify(board)); // Debugging log
                clearBoard(); // Clear the board before rendering
                renderBoard(board); // Render the board with the fetched data
                updateGameStatus(board); // Update the game status based on the board
            })
            .catch(error => console.error("Error fetching initial board:", error)); // Handle errors
    }

    // Function to fetch and display the current game rule
    function fetchCurrentRule() {
        fetch("/api/game/currentRule")
            .then(response => response.text())
            .then(rule => {
                ruleDisplay.textContent = `Current Rule: ${rule}`; // Display the current rule
                updateSpecificRule(rule); // Update the specific rule description
            })
            .catch(error => console.error("Error fetching current rule:", error)); // Handle errors
    }

    // Function to update the specific rule description based on the current rule
    function updateSpecificRule(rule) {
        let specificRuleText = "";
        // Update the description text based on the rule name
        switch (rule) {
            case "PawnPromotionRule":
                specificRuleText = "After the Soldier captures an opponent's piece for the first time, it randomly becomes a Knight or Bishop. On the second capture, it randomly turns into a Cannon or Rook. After the third capture, it becomes a Queen. Further captures will not change the piece.";
                break;
            case "KingQueenSpecialRule":
                specificRuleText = "After the Queen or King captures an opponent's piece for the first time, it converts that piece into one of its own.";
                break;
            case "CannonSpecialRule":
                specificRuleText = "After a Cannon captures three pieces, it explodes and removes one enemy piece in each direction: front, back, left, and right.";
                break;
            default:
                specificRuleText = "";
                break;
        }
        specificRuleDisplay.textContent = `Specific Rule: ${specificRuleText}`; // Display the specific rule
    }

    // Function to render the board with pieces based on the board data
    function renderBoard(board) {
        console.log("Rendering board with data:", JSON.stringify(board)); // Debugging log
        clearBoard(); // Ensure the board is cleared before rendering
        board.forEach((row, rowIndex) => {
            row.forEach((piece, colIndex) => {
                const square = document.createElement("div");
                square.classList.add("square"); // Add class for styling
                square.dataset.row = rowIndex; // Set row data attribute
                square.dataset.col = colIndex; // Set column data attribute
                // Alternate square colors for the chessboard pattern
                if ((rowIndex + colIndex) % 2 === 0) {
                    square.style.backgroundColor = "#f0d9b5";
                } else {
                    square.style.backgroundColor = "#b58863";
                }

                if (piece) { // If there is a piece on this square
                    const img = document.createElement("img");
                    img.src = `images/${piece.color.toLowerCase()}${piece.type}.png`; // Set image source based on piece type and color
                    img.classList.add("piece"); // Add class for styling
                    img.draggable = true; // Make the piece draggable
                    img.dataset.row = rowIndex; // Set row data attribute
                    img.dataset.col = colIndex; // Set column data attribute
                    img.dataset.piece = piece.type; // Set piece type data attribute
                    img.dataset.color = piece.color.toLowerCase(); // Set piece color data attribute
                    img.dataset.captureCount = piece.captureCount || 0; // Set capture count
                    img.dataset.isFirstMove = piece.firstMove !== undefined ? piece.firstMove : "true"; // Handle first move
                    if (piece.immobile) { // If the piece is immobile
                        img.classList.add('immobile'); // Add immobile class
                    }
                    square.appendChild(img); // Add the piece to the square
                }
                chessboard.appendChild(square); // Add the square to the chessboard
            });
        });

        addDragAndDropListeners(); // Add drag-and-drop event listeners
        addHoverListeners(); // Add hover event listeners
    }

    // Function to add drag-and-drop event listeners to pieces and squares
    function addDragAndDropListeners() {
        const pieces = document.querySelectorAll(".piece");
        pieces.forEach(piece => {
            piece.addEventListener("dragstart", handleDragStart); // Handle drag start event
        });

        const squares = document.querySelectorAll(".square");
        squares.forEach(square => {
            square.addEventListener("dragover", handleDragOver); // Handle drag over event
            square.addEventListener("drop", handleDrop); // Handle drop event
        });
    }

    // Function to add mouse hover event listeners to the chessboard
    function addHoverListeners() {
        chessboard.addEventListener('mouseover', handleMouseOver); // Handle mouse over event
        chessboard.addEventListener('mouseout', handleMouseOut); // Handle mouse out event
    }

    // Event handler for mouse over event on chessboard
    function handleMouseOver(event) {
        const piece = event.target.closest('.piece'); // Get the closest piece element
        if (piece) { // If a piece is being hovered over
            const startX = parseInt(piece.dataset.row); // Get starting X position
            const startY = parseInt(piece.dataset.col); // Get starting Y position
            const color = piece.dataset.color.toUpperCase(); // Get piece color
            const pieceType = piece.dataset.piece; // Get piece type
            const payload = { // Create payload with move information
                startX: startX,
                startY: startY,
                piece: pieceType,
                color: color
            };

            // Fetch valid moves for the hovered piece
            fetch("/api/game/validMoves", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            })
                .then(response => {
                    if (!response.ok) { // Check if response is okay
                        throw new Error(`HTTP error, status = ${response.status}`); // Throw error if not
                    }
                    return response.json(); // Parse JSON response
                })
                .then(validMoves => {
                    if (Array.isArray(validMoves)) { // Check if validMoves is an array
                        highlightValidMoves(piece, validMoves); // Highlight the valid moves
                    } else {
                        console.error('Expected an array of valid moves, got:', validMoves); // Log error if not
                    }
                })
                .catch(error => {
                    console.error("Error fetching valid moves:", error); // Handle errors
                });
        }
    }

    // Event handler for mouse out event on chessboard
    function handleMouseOut(event) {
        clearHighlights(); // Clear highlighted moves when mouse leaves the piece
    }

    // Function to highlight valid moves for a piece
    function highlightValidMoves(piece, validMoves) {
        clearHighlights(); // Clear previous highlights
        validMoves.forEach(move => {
            const targetSquare = document.querySelector(`.square[data-row="${move.endX}"][data-col="${move.endY}"]`);
            if (targetSquare) { // If the target square is found
                targetSquare.classList.add('highlight-move'); // Add class to highlight the move
                if ((move.endX + move.endY) % 2 === 0) { // Alternate highlight color for even squares
                    targetSquare.classList.add('even');
                }
            }
        });
    }

    // Function to clear highlighted moves on the chessboard
    function clearHighlights() {
        const highlightedSquares = document.querySelectorAll('.highlight-move'); // Get all highlighted squares
        highlightedSquares.forEach(square => {
            square.classList.remove('highlight-move'); // Remove highlight class
            square.classList.remove('even'); // Remove even class if present
        });
    }

    // Event handler for drag start event on pieces
    function handleDragStart(event) {
        const color = event.target.dataset.color; // Get the color of the piece being dragged
        if ((isWhiteTurn && color === "white") || (!isWhiteTurn && color === "black")) { // Check if it's the correct turn
            console.log("Drag start:", event.target.dataset); // Log the drag start event
            event.dataTransfer.setData("text/plain", JSON.stringify({ // Set the drag data
                startX: parseInt(event.target.dataset.row),  // Ensure it's an integer
                startY: parseInt(event.target.dataset.col),  // Ensure it's an integer
                endX: null, // No end position when drag starts
                endY: null, // No end position when drag starts
                piece: event.target.dataset.piece,
                color: event.target.dataset.color
            }));
        } else {
            event.preventDefault(); // Prevent drag if it's not the correct turn
            alert("It's not your turn!"); // Alert the player
        }
    }

    // Event handler for drop event on squares
    function handleDrop(event) {
        event.preventDefault(); // Prevent default drop behavior
        const startData = JSON.parse(event.dataTransfer.getData("text/plain")); // Get the drag data
        console.log("Drop data:", startData); // Log the drop data

        const target = event.target.closest('.square'); // Get the closest square element
        if (!target) return; // Return if no target square is found

        const move = { // Create move object with start and end positions
            startX: startData.startX,
            startY: startData.startY,
            endX: parseInt(target.dataset.row),  // Ensure it's an integer
            endY: parseInt(target.dataset.col),  // Ensure it's an integer
            piece: startData.piece,
            color: startData.color
        };

        if ((isWhiteTurn && move.color === "white") || (!isWhiteTurn && move.color === "black")) { // Check if it's the correct turn
            validateAndMovePiece(move); // Validate and move the piece
        } else {
            alert("It's not your turn!"); // Alert the player
        }
    }

    // Event handler for drag over event on squares
    function handleDragOver(event) {
        event.preventDefault(); // Prevent default drag over behavior to allow dropping
    }

    // Function to validate and execute a piece move
    function validateAndMovePiece(move) {
        console.log("Validating move:", move); // Log the move being validated
        fetch("/api/game/movePiece", { // Send a POST request to validate the move
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(move) // Send move data as JSON
        })
            .then(response => {
                if (!response.ok) { // Check if the response is okay
                    throw new Error(`HTTP error, status = ${response.status}`); // Throw error if not
                }
                return response.text(); // Parse the response text
            })
            .then(result => {
                console.log("Move result from server:", result); // Log the server's move result

                const [moveResult, currentTurnInfo] = result.split(';'); // Split the result into moveResult and currentTurnInfo

                if (moveResult === "VALID_MOVE") { // Check if the move is valid
                    updateBoardWithMove(move); // Update the board with the new move
                    if (currentTurnInfo.endsWith("BLACK")) { // If it's now Black's turn
                        setTimeout(aiMove, 500); // Call AI move with a 0.5-second delay
                    }
                }

                if (currentTurnInfo.startsWith("CURRENT_TURN=")) { // Check if the response contains current turn information
                    const turn = currentTurnInfo.split('=')[1]; // Extract the current turn
                    isWhiteTurn = (turn === "WHITE"); // Update the turn variable
                    console.log("Updated current turn:", turn); // Log the current turn
                }

                if (moveResult === "WHITE_WINS" || moveResult === "BLACK_WINS") { // Check if the game has ended with a win
                    alert(`Game Over: ${moveResult}`); // Alert the players of the game result
                } else if (moveResult === "STALEMATE") { // Check if the game ended in a stalemate
                    alert("Game Drawn: Stalemate"); // Alert the players of a draw
                } else if (moveResult === "INVALID_MOVE") { // Check if the move was invalid
                    alert("Invalid move"); // Alert the player of an invalid move
                }
            })
            .catch(error => console.error("Error processing move:", error)); // Handle errors
    }

    // Function to update the board state and animate the piece move
    function updateBoardWithMove(move) {
        const startSquare = document.querySelector(`.square[data-row="${move.startX}"][data-col="${move.startY}"]`); // Get the start square
        const targetSquare = document.querySelector(`.square[data-row="${move.endX}"][data-col="${move.endY}"]`); // Get the target square

        if (!startSquare || !targetSquare) { // Check if either square is invalid
            console.error("Invalid start or target square"); // Log an error
            return;
        }

        const piece = document.querySelector(`.piece[data-row="${move.startX}"][data-col="${move.startY}"]`); // Get the piece being moved
        if (!piece) { // Check if the piece exists
            console.error("No piece found at the start position"); // Log an error
            return;
        }

        console.log(`Move from (${move.startX}, ${move.startY}) to (${move.endX}, ${move.endY})`); // Log the move

        const isCapture = targetSquare.firstChild !== null; // Determine if the move captures a piece

        // Calculate the offset for the piece animation
        const startRect = startSquare.getBoundingClientRect(); // Get the starting rectangle
        const targetRect = targetSquare.getBoundingClientRect(); // Get the target rectangle
        const moveX = targetRect.left - startRect.left; // Calculate horizontal movement
        const moveY = targetRect.top - startRect.top; // Calculate vertical movement

        // Apply CSS transition for the animation
        piece.style.transition = "transform 0.3s ease"; // Set transition style
        piece.style.transform = `translate(${moveX}px, ${moveY}px)`; // Apply translation for movement

        // Event listener for the end of the animation to update the board state
        piece.addEventListener("transitionend", () => {
            piece.style.transition = ""; // Clear the transition style
            piece.style.transform = ""; // Clear the transform style

            if (isCapture) { // If a piece is captured
                const capturedPiece = targetSquare.firstChild; // Get the captured piece
                console.log(`Captured Piece: ${capturedPiece.dataset.piece}, Color: ${capturedPiece.dataset.color}`); // Log captured piece

                if (piece.dataset.piece === 'King' || piece.dataset.piece === 'Queen') { // Special handling for King/Queen captures
                    console.log("Applying special capture logic for King/Queen");
                    // Change the captured piece to the color of the capturing piece
                    capturedPiece.dataset.color = piece.dataset.color;
                    capturedPiece.src = `images/${capturedPiece.dataset.color}${capturedPiece.dataset.piece}.png`;

                    const deltaX = move.endX - move.startX; // Calculate movement in X
                    const deltaY = move.endY - move.startY; // Calculate movement in Y
                    const newX = move.endX - Math.sign(deltaX); // New X position for King/Queen
                    const newY = move.endY - Math.sign(deltaY); // New Y position for King/Queen

                    console.log(`New Position for King/Queen: (${newX}, ${newY})`);

                    const newSquare = document.querySelector(`.square[data-row="${newX}"][data-col="${newY}"]`); // Get the new square for the piece
                    if (!newSquare) { // Check if the new square is valid
                        console.error("Invalid new square position"); // Log an error
                        return;
                    }

                    // Move the piece to the new square
                    if (newSquare.firstChild) {
                        newSquare.removeChild(newSquare.firstChild); // Clear any existing piece
                    }
                    piece.dataset.row = newX; // Update piece's row data
                    piece.dataset.col = newY; // Update piece's column data
                    newSquare.appendChild(piece); // Add piece to the new square

                    // Update the target square with the captured piece
                    targetSquare.innerHTML = ""; // Clear the target square
                    targetSquare.appendChild(capturedPiece); // Add captured piece
                } else {
                    // Normal capture logic
                    targetSquare.removeChild(capturedPiece); // Remove captured piece
                    piece.dataset.row = move.endX; // Update piece's row data
                    piece.dataset.col = move.endY; // Update piece's column data
                    targetSquare.appendChild(piece); // Add piece to the target square
                }
            } else {
                // Normal move
                piece.dataset.row = move.endX; // Update piece's row data
                piece.dataset.col = move.endY; // Update piece's column data
                targetSquare.appendChild(piece); // Add piece to the target square
            }

            // Clear the starting square
            startSquare.innerHTML = "";

            // Handle cannon explosion logic
            if (piece.dataset.piece === 'Cannon' && parseInt(piece.dataset.captureCount) >= 3) {
                console.log("Cannon is exploding!");
                const directions = [ // Define directions for explosion
                    { x: 1, y: 0 }, { x: -1, y: 0 }, { x: 0, y: 1 }, { x: 0, y: -1 }
                ];
                directions.forEach(dir => { // Check each direction
                    const x = move.endX + dir.x;
                    const y = move.endY + dir.y;
                    if (x >= 0 && x < 8 && y >= 0 && y < 8) { // Ensure within board bounds
                        const adjacentSquare = document.querySelector(`.square[data-row="${x}"][data-col="${y}"]`);
                        if (adjacentSquare) { // If square is valid
                            const adjacentPiece = adjacentSquare.firstChild;
                            if (adjacentPiece && adjacentPiece.dataset.color !== piece.dataset.color) {
                                console.log(`Removing adjacent piece at (${x}, ${y})`);
                                adjacentSquare.removeChild(adjacentPiece); // Remove enemy piece
                            }
                        }
                    }
                });
                targetSquare.removeChild(piece); // Remove the exploding cannon
            }

            // Handle pawn promotion logic
            if ((piece.dataset.piece === 'Pawn' || piece.dataset.promotedFromPawn === "true") && isCapture) {
                const captureCount = parseInt(piece.dataset.captureCount); // Get capture count
                let promotedType = null; // Initialize promoted type

                // Determine promotion based on capture count
                if (captureCount === 1) {
                    promotedType = Math.random() < 0.5 ? 'Knight' : 'Bishop';
                } else if (captureCount === 2) {
                    promotedType = Math.random() < 0.5 ? 'Cannon' : 'Rook';
                } else if (captureCount === 3) {
                    promotedType = 'Queen';
                }

                if (promotedType) { // If a promotion type is determined
                    console.log(`Pawn promoted to ${promotedType}`);
                    piece.dataset.piece = promotedType; // Update piece type
                    piece.dataset.promotedFromPawn = "true"; // Set promoted flag
                    piece.src = `images/${piece.dataset.color}${promotedType}.png`; // Update piece image
                }
            }

            fetchUpdatedBoard(); // Fetch updated board state after changes
        }, { once: true });
    }

    // Function to fetch and update the board with the latest state
    function fetchUpdatedBoard() {
        fetch("/api/game/board")
            .then(response => response.json())
            .then(board => {
                console.log("Fetched updated board:", JSON.stringify(board)); // Debugging log
                clearBoard(); // Clear the board before rendering
                renderBoard(board); // Render the board with fetched data
                updateGameStatus(board); // Update game status based on the current board
            })
            .catch(error => console.error("Error fetching updated board:", error)); // Handle errors
    }

    // Function to update the game status indicators based on the board state
    function updateGameStatus(board) {
        let whiteCount = 0, blackCount = 0; // Initialize counters for white and black pieces
        board.forEach(row => {
            row.forEach(piece => {
                if (piece) { // Check if there is a piece
                    if (piece.color === "WHITE") whiteCount++; // Increment white piece count
                    if (piece.color === "BLACK") blackCount++; // Increment black piece count
                }
            });
        });

        const totalPieces = whiteCount + blackCount; // Calculate total number of pieces
        const whiteAdvantage = ((whiteCount - blackCount) / totalPieces) * 100; // Calculate white advantage percentage
        const progressBar = document.getElementById('advantageBar'); // Get the advantage progress bar
        progressBar.style.width = `${50 + whiteAdvantage / 2}%`; // Center at 50%, adjust according to advantage

        const advantageText = document.getElementById('advantageColor'); // Get the advantage text element
        if (whiteCount > blackCount) { // Check if white has an advantage
            advantageText.textContent = "White has the advantage"; // Update text
            document.getElementById('status').textContent = "Ahead"; // Update status
        } else if (blackCount > whiteCount) { // Check if black has an advantage
            advantageText.textContent = "Black has the advantage"; // Update text
            document.getElementById('status').textContent = "Behind"; // Update status
        } else {
            advantageText.textContent = "Neither side"; // Update text for equal pieces
            document.getElementById('status').textContent = "Draw"; // Update status
        }
    }

    // Function to clear the chessboard
    function clearBoard() {
        console.log("Clearing board elements..."); // Debugging log
        while (chessboard.firstChild) { // While there are child elements in the chessboard
            console.log("Removing element:", chessboard.firstChild); // Log the element being removed
            chessboard.removeChild(chessboard.firstChild); // Remove the first child
        }
        console.log("Board cleared."); // Debugging log
    }

    // Event listener for the restart button
    restartButton.addEventListener("click", function() {
        console.log("Restart button clicked, calling restart endpoint."); // Debugging log
        fetch("/api/game/restart", { // Send a POST request to restart the game
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(() => {
            console.log("Board cleared and initial state fetched."); // Debugging log
            clearBoard(); // Clear the board
            fetchInitialBoard(); // Fetch and display the initial board state
            fetchCurrentRule(); // Fetch and display the current rule
            isWhiteTurn = true; // Reset turn to White
        }).catch(error => console.error("Error restarting game:", error)); // Handle errors
    });

    // Function to animate piece movement on the board
    function movePieceWithAnimation(piece, startSquare, targetSquare) {
        // Calculate the translation values for the piece
        const startRect = startSquare.getBoundingClientRect(); // Get start square dimensions
        const targetRect = targetSquare.getBoundingClientRect(); // Get target square dimensions
        const moveX = targetRect.left - startRect.left; // Calculate horizontal movement
        const moveY = targetRect.top - startRect.top; // Calculate vertical movement

        // Set CSS variables for animation
        piece.style.setProperty('--move-x', `${moveX}px`); // Set X translation
        piece.style.setProperty('--move-y', `${moveY}px`); // Set Y translation

        // Add animation class for movement
        piece.classList.add('animate-move');

        // Event listener for the end of the animation to reset position and update the board
        piece.addEventListener('animationend', () => {
            piece.classList.remove('animate-move'); // Remove animation class
            piece.style.removeProperty('--move-x'); // Clear X translation
            piece.style.removeProperty('--move-y'); // Clear Y translation
            updateBoardAfterMove(piece, targetSquare); // Ensure board reflects final state
        }, { once: true }); // Ensure listener only triggers once
    }

    // Function to update the board state after a move
    function updateBoardAfterMove(piece, targetSquare) {
        // Update the piece's new data attributes
        piece.dataset.row = targetSquare.dataset.row; // Update row data
        piece.dataset.col = targetSquare.dataset.col; // Update column data

        // Append the piece to the new square
        targetSquare.appendChild(piece); // Add piece to target square

        // Clear the old square
        const startSquare = document.querySelector(`.square[data-row="${piece.dataset.row}"][data-col="${piece.dataset.col}"]`);
        if (startSquare) {
            startSquare.innerHTML = ""; // Clear content of the start square
        }

        fetchUpdatedBoard(); // Fetch and display the updated board state
    }

    // Function for AI to make a move
    function aiMove() {
        setTimeout(() => { // Delay AI move slightly
            fetch("/api/game/aiMove")
                .then(response => response.json())
                .then(aiMove => {
                    console.log("AI move received:", aiMove); // Log the AI move
                    const piece = document.querySelector(`.piece[data-row="${aiMove.startX}"][data-col="${aiMove.startY}"]`);
                    const startSquare = document.querySelector(`.square[data-row="${aiMove.startX}"][data-col="${aiMove.startY}"]`);
                    const targetSquare = document.querySelector(`.square[data-row="${aiMove.endX}"][data-col="${aiMove.endY}"]`);
                    updateBoardWithMove(aiMove); // Ensure all pieces apply animation when AI moves
                })
                .catch(error => console.error("Error fetching AI move:", error)); // Handle errors
        }, 500); // Delay of 0.5 seconds
    }

    fetchInitialBoard(); // Initial fetch to set up the board
    fetchCurrentRule(); // Fetch and display the current rule
});
