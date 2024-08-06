document.addEventListener("DOMContentLoaded", function() {
    const chessboard = document.getElementById("chessboard");
    const ruleDisplay = document.getElementById("ruleDisplay");
    const specificRuleDisplay = document.getElementById("specificRuleDisplay");
    const restartButton = document.getElementById("restartButton");
    let isWhiteTurn = true;
    const undoButton = document.getElementById("undoButton");
    const redoButton = document.getElementById("redoButton");

    document.getElementById("cannonExplodeBtn").addEventListener("click", function() {
        console.log("CannonSpecialRule button clicked");
        setGameRule("CannonSpecialRule");
    });

    document.getElementById("kingQueenCaptureBtn").addEventListener("click", function() {
        console.log("KingQueenSpecialRule button clicked");
        setGameRule("KingQueenSpecialRule");
    });

    document.getElementById("pawnPromotionBtn").addEventListener("click", function() {
        console.log("PawnPromotionRule button clicked");
        setGameRule("PawnPromotionRule");
    });

    function setGameRule(rule) {
        console.log(`Setting game rule to: ${rule}`); // Debugging line
        fetch(`/api/game/setRule/${rule}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(() => {
            console.log("Game rule set successfully, resetting board."); // Debugging line
            clearBoard();
            fetchInitialBoard();
            fetchCurrentRule();
            isWhiteTurn = true;
        }).catch(error => console.error("Error setting game rule:", error));
    }

    // Function to handle undo button click
    undoButton.addEventListener("click", function() {
        fetch("/api/game/undo", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.text())
            .then(result => {
                if (result === "UNDO_SUCCESS") {
                    fetchUpdatedBoard(); // Refresh the board
                } else {
                    alert("No move to undo!");
                }
            })
            .catch(error => console.error("Error undoing move:", error));
    });

    // Function to handle redo button click
    redoButton.addEventListener("click", function() {
        fetch("/api/game/redo", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(response => response.text())
            .then(result => {
                if (result === "REDO_SUCCESS") {
                    fetchUpdatedBoard(); // Refresh the board
                } else {
                    alert("No move to redo!");
                }
            })
            .catch(error => console.error("Error redoing move:", error));
    });
    function fetchInitialBoard() {
        fetch("/api/game/initialBoard")
            .then(response => response.json())
            .then(board => {
                console.log("Fetched initial board:", JSON.stringify(board));
                clearBoard();
                renderBoard(board);
            })
            .catch(error => console.error("Error fetching initial board:", error));
    }

    function fetchCurrentRule() {
        fetch("/api/game/currentRule")
            .then(response => response.text())
            .then(rule => {
                ruleDisplay.textContent = `Current Rule: ${rule}`;
                updateSpecificRule(rule);
            })
            .catch(error => console.error("Error fetching current rule:", error));
    }

    function updateSpecificRule(rule) {
        let specificRuleText = "";
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
        specificRuleDisplay.textContent = `Specific Rule: ${specificRuleText}`;
    }

    function renderBoard(board) {
        console.log("Rendering board with data:", JSON.stringify(board));
        clearBoard(); // Ensure the board is cleared before rendering
        board.forEach((row, rowIndex) => {
            row.forEach((piece, colIndex) => {
                const square = document.createElement("div");
                square.classList.add("square");
                square.dataset.row = rowIndex;
                square.dataset.col = colIndex;
                if ((rowIndex + colIndex) % 2 === 0) {
                    square.style.backgroundColor = "#f0d9b5";
                } else {
                    square.style.backgroundColor = "#b58863";
                }

                if (piece) {
                    const img = document.createElement("img");
                    img.src = `images/${piece.color.toLowerCase()}${piece.type}.png`;
                    img.classList.add("piece");
                    img.draggable = true;
                    img.dataset.row = rowIndex;
                    img.dataset.col = colIndex;
                    img.dataset.piece = piece.type;
                    img.dataset.color = piece.color.toLowerCase();
                    img.dataset.captureCount = piece.captureCount || 0;
                    img.dataset.isFirstMove = piece.firstMove !== undefined ? piece.firstMove : "true"; // Correctly handle the first move
                    if (piece.immobile) {
                        img.classList.add('immobile');
                    }
                    square.appendChild(img);
                }
                chessboard.appendChild(square);
            });
        });

        addDragAndDropListeners();
        addHoverListeners();
    }

    function addDragAndDropListeners() {
        const pieces = document.querySelectorAll(".piece");
        pieces.forEach(piece => {
            piece.addEventListener("dragstart", handleDragStart);
        });

        const squares = document.querySelectorAll(".square");
        squares.forEach(square => {
            square.addEventListener("dragover", handleDragOver);
            square.addEventListener("drop", handleDrop);
        });
    }

    function addHoverListeners() {
        chessboard.addEventListener('mouseover', handleMouseOver);
        chessboard.addEventListener('mouseout', handleMouseOut);
    }

    function handleMouseOver(event) {
        const piece = event.target.closest('.piece');
        if (piece) {
            const startX = parseInt(piece.dataset.row);
            const startY = parseInt(piece.dataset.col);
            const color = piece.dataset.color.toUpperCase();
            const pieceType = piece.dataset.piece;
            const payload = {
                startX: startX,
                startY: startY,
                piece: pieceType,
                color: color
            };

            fetch("/api/game/validMoves", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error, status = ${response.status}`);
                    }
                    return response.json();
                })
                .then(validMoves => {
                    if (Array.isArray(validMoves)) {
                        highlightValidMoves(piece, validMoves);
                    } else {
                        console.error('Expected an array of valid moves, got:', validMoves);
                    }
                })
                .catch(error => {
                    console.error("Error fetching valid moves:", error);
                });
        }
    }

    function handleMouseOut(event) {
        clearHighlights();
    }

    function highlightValidMoves(piece, validMoves) {
        clearHighlights();
        validMoves.forEach(move => {
            const targetSquare = document.querySelector(`.square[data-row="${move.endX}"][data-col="${move.endY}"]`);
            if (targetSquare) {
                targetSquare.classList.add('highlight-move');
                if ((move.endX + move.endY) % 2 === 0) {
                    targetSquare.classList.add('even');
                }
            }
        });
    }

    function clearHighlights() {
        const highlightedSquares = document.querySelectorAll('.highlight-move');
        highlightedSquares.forEach(square => {
            square.classList.remove('highlight-move');
            square.classList.remove('even');
        });
    }

    function handleDragStart(event) {
        const color = event.target.dataset.color;
        if ((isWhiteTurn && color === "white") || (!isWhiteTurn && color === "black")) {
            console.log("Drag start:", event.target.dataset);
            event.dataTransfer.setData("text/plain", JSON.stringify({
                startX: parseInt(event.target.dataset.row),  // Ensure it's an integer
                startY: parseInt(event.target.dataset.col),  // Ensure it's an integer
                endX: null, // No end position when drag starts
                endY: null, // No end position when drag starts
                piece: event.target.dataset.piece,
                color: event.target.dataset.color
            }));
        } else {
            event.preventDefault();
            alert("It's not your turn!");
        }
    }

    function handleDrop(event) {
        event.preventDefault();
        const startData = JSON.parse(event.dataTransfer.getData("text/plain"));
        console.log("Drop data:", startData);

        const target = event.target.closest('.square');
        if (!target) return;

        const move = {
            startX: startData.startX,
            startY: startData.startY,
            endX: parseInt(target.dataset.row),  // Ensure it's an integer
            endY: parseInt(target.dataset.col),  // Ensure it's an integer
            piece: startData.piece,
            color: startData.color
        };

        if ((isWhiteTurn && move.color === "white") || (!isWhiteTurn && move.color === "black")) {
            validateAndMovePiece(move);
        } else {
            alert("It's not your turn!");
        }
    }

    function handleDragOver(event) {
        event.preventDefault();
    }

    function validateAndMovePiece(move) {
        console.log("Validating move:", move);
        fetch("/api/game/movePiece", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(move)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error, status = ${response.status}`);
                }
                return response.text();
            })
            .then(result => {
                console.log("Move result from server:", result);

                const [moveResult, currentTurnInfo] = result.split(';');

                if (moveResult === "VALID_MOVE") {
                    updateBoardWithMove(move); // Update the board with the move
                }

                if (currentTurnInfo.startsWith("CURRENT_TURN=")) {
                    const turn = currentTurnInfo.split('=')[1];
                    isWhiteTurn = (turn === "WHITE");
                    console.log("Updated current turn:", turn);
                }

                if (moveResult === "WHITE_WINS" || moveResult === "BLACK_WINS") {
                    alert(`Game Over: ${moveResult}`);
                } else if (moveResult === "STALEMATE") {
                    alert("Game Drawn: Stalemate");
                } else if (moveResult === "INVALID_MOVE") {
                    alert("Invalid move");
                }
            })
            .catch(error => console.error("Error processing move:", error));
    }



    function fetchCurrentTurn() {
        fetch("/api/game/currentTurn")
            .then(response => response.text())
            .then(turn => {
                isWhiteTurn = (turn === "WHITE");
                console.log("Current turn:", turn);
            })
            .catch(error => console.error("Error fetching current turn:", error));
    }

    function updateBoardWithMove(move) {
        const startSquare = document.querySelector(`.square[data-row="${move.startX}"][data-col="${move.startY}"]`);
        const targetSquare = document.querySelector(`.square[data-row="${move.endX}"][data-col="${move.endY}"]`);

        // Ensure both startSquare and targetSquare are valid
        if (!startSquare || !targetSquare) {
            console.error("Invalid start or target square");
            return;
        }

        console.log(`Move from (${move.startX}, ${move.startY}) to (${move.endX}, ${move.endY})`);

        const piece = document.querySelector(`.piece[data-row="${move.startX}"][data-col="${move.startY}"]`);
        if (!piece) {
            console.error("No piece found at the start position");
            return;
        }

        console.log(`Piece: ${piece.dataset.piece}, Color: ${piece.dataset.color}`);

        const isCapture = targetSquare.firstChild !== null;

        // Define animation durations
        const aiMoveDelay = 1;
        const blackMoveDuration = 10;

        // Function to handle piece animation and movement
        function animateMove() {
            const startRect = startSquare.getBoundingClientRect();
            const targetRect = targetSquare.getBoundingClientRect();
            const moveX = targetRect.left - startRect.left;
            const moveY = targetRect.top - startRect.top;

            // Apply transition only to black pieces
            if (piece.dataset.color === 'black') {
                setTimeout(() => {
                    piece.style.transition = `transform ${blackMoveDuration}ms ease`;
                    piece.style.transform = `translate(${moveX}px, ${moveY}px)`;

                    piece.addEventListener("transitionend", finalizeMove, { once: true });
                }, aiMoveDelay);
            } else {
                piece.style.transition = "";
                piece.style.transform = `translate(${moveX}px, ${moveY}px)`;
                finalizeMove();
            }
        }

        // Function to finalize move after animation
        function finalizeMove() {
            piece.style.transition = "";
            piece.style.transform = "";

            if (isCapture) {
                const capturedPiece = targetSquare.firstChild;
                console.log(`Captured Piece: ${capturedPiece.dataset.piece}, Color: ${capturedPiece.dataset.color}`);

                if (piece.dataset.piece === 'King' || piece.dataset.piece === 'Queen') {
                    console.log("Applying special capture logic for King/Queen");
                    // Special logic for King or Queen capturing
                    capturedPiece.dataset.color = piece.dataset.color;
                    capturedPiece.src = `images/${capturedPiece.dataset.color}${capturedPiece.dataset.piece}.png`;

                    const deltaX = move.endX - move.startX;
                    const deltaY = move.endY - move.startY;
                    const newX = move.endX - Math.sign(deltaX);
                    const newY = move.endY - Math.sign(deltaY);

                    console.log(`New Position for King/Queen: (${newX}, ${newY})`);

                    const newSquare = document.querySelector(`.square[data-row="${newX}"][data-col="${newY}"]`);
                    if (!newSquare) {
                        console.error("Invalid new square position");
                        return;
                    }

                    // Move the piece to the new square
                    if (newSquare.firstChild) {
                        newSquare.removeChild(newSquare.firstChild);
                    }
                    piece.dataset.row = newX;
                    piece.dataset.col = newY;
                    newSquare.appendChild(piece);

                    // Update the target square with the captured piece
                    targetSquare.innerHTML = "";
                    targetSquare.appendChild(capturedPiece);
                } else {
                    // Normal capture logic
                    targetSquare.removeChild(capturedPiece);
                    piece.dataset.row = move.endX;
                    piece.dataset.col = move.endY;
                    targetSquare.appendChild(piece);
                }
            } else {
                // Normal move
                piece.dataset.row = move.endX;
                piece.dataset.col = move.endY;
                targetSquare.appendChild(piece);
            }

            // Clear the start square
            startSquare.innerHTML = "";

            // Handle Cannon explosion logic
            if (piece.dataset.piece === 'Cannon' && parseInt(piece.dataset.captureCount) >= 3) {
                console.log("Cannon is exploding!");
                const directions = [
                    { x: 1, y: 0 }, { x: -1, y: 0 }, { x: 0, y: 1 }, { x: 0, y: -1 }
                ];
                directions.forEach(dir => {
                    const x = move.endX + dir.x;
                    const y = move.endY + dir.y;
                    if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                        const adjacentSquare = document.querySelector(`.square[data-row="${x}"][data-col="${y}"]`);
                        if (adjacentSquare) {
                            const adjacentPiece = adjacentSquare.firstChild;
                            if (adjacentPiece && adjacentPiece.dataset.color !== piece.dataset.color) {
                                console.log(`Removing adjacent piece at (${x}, ${y})`);
                                adjacentSquare.removeChild(adjacentPiece);
                            }
                        }
                    }
                });
                targetSquare.removeChild(piece);
            }

            // Handle Pawn promotion logic
            if ((piece.dataset.piece === 'Pawn' || piece.dataset.promotedFromPawn === "true") && isCapture) {
                const captureCount = parseInt(piece.dataset.captureCount);
                let promotedType = null;

                if (captureCount === 1) {
                    promotedType = Math.random() < 0.5 ? 'Knight' : 'Bishop';
                } else if (captureCount === 2) {
                    promotedType = Math.random() < 0.5 ? 'Cannon' : 'Rook';
                } else if (captureCount === 3) {
                    promotedType = 'Queen';
                }

                if (promotedType) {
                    console.log(`Pawn promoted to ${promotedType}`);
                    piece.dataset.piece = promotedType;
                    piece.dataset.promotedFromPawn = "true"; // Ensure the promotion flag is set
                    piece.src = `images/${piece.dataset.color}${promotedType}.png`;
                }
            }

            // Fetch updated board state after changes
            fetchUpdatedBoard();
        }

        animateMove();
    }


    function fetchUpdatedBoard() {
        fetch("/api/game/board")
            .then(response => response.json())
            .then(newBoard => {
                console.log("Fetched updated board:", JSON.stringify(newBoard));
                clearBoard();
                renderBoard(newBoard);

                // Fetch current turn and update isWhiteTurn variable
                fetchCurrentTurn();
            })
            .catch(error => console.error("Error fetching updated board:", error));
    }

    function clearBoard() {
        console.log("Clearing board elements...");
        while (chessboard.firstChild) {
            console.log("Removing element:", chessboard.firstChild);
            chessboard.removeChild(chessboard.firstChild);
        }
        console.log("Board cleared.");
    }

    restartButton.addEventListener("click", function() {
        console.log("Restart button clicked, calling restart endpoint.");
        fetch("/api/game/restart", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(() => {
            console.log("Board cleared and initial state fetched.");
            clearBoard();
            fetchInitialBoard();
            fetchCurrentRule();
            isWhiteTurn = true;
        }).catch(error => console.error("Error restarting game:", error));
    });
    function movePieceWithAnimation(piece, startSquare, targetSquare) {
        // Calculate the translation values
        const startRect = startSquare.getBoundingClientRect();
        const targetRect = targetSquare.getBoundingClientRect();
        const moveX = targetRect.left - startRect.left;
        const moveY = targetRect.top - startRect.top;

        // Set CSS variables for animation
        piece.style.setProperty('--move-x', `${moveX}px`);
        piece.style.setProperty('--move-y', `${moveY}px`);

        // Add animation class
        piece.classList.add('animate-move');

        // After the animation ends, reset position and update board
        piece.addEventListener('animationend', () => {
            piece.classList.remove('animate-move');
            piece.style.removeProperty('--move-x');
            piece.style.removeProperty('--move-y');
            updateBoardAfterMove(piece, targetSquare); // Ensure board reflects final state
        }, { once: true });
    }
    function updateBoardAfterMove(piece, targetSquare) {
        // Update piece's new data attributes
        piece.dataset.row = targetSquare.dataset.row;
        piece.dataset.col = targetSquare.dataset.col;

        // Append the piece to the new square
        targetSquare.appendChild(piece);

        // Clear the old square
        const startSquare = document.querySelector(`.square[data-row="${piece.dataset.row}"][data-col="${piece.dataset.col}"]`);
        if (startSquare) {
            startSquare.innerHTML = "";
        }

        fetchUpdatedBoard();
    }
    function aiMove() {
        setTimeout(() => {
            // Fetch AI move and then animate it
            fetch("/api/game/aiMove") // Ensure this endpoint returns the AI move
                .then(response => response.json())
                .then(aiMove => {
                    console.log("AI move received:", aiMove);
                    updateBoardWithMove(aiMove); // Animate AI's move
                })
                .catch(error => console.error("Error fetching AI move:", error));
        }, 1000); // Delay AI move by 1000ms (1 second) for better visibility
    }
    fetchInitialBoard();
    fetchCurrentRule();
});
