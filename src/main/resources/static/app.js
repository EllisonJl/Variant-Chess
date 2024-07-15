document.addEventListener("DOMContentLoaded", function() {
    const chessboard = document.getElementById("chessboard");
    let isWhiteTurn = true; // 白棋先行

    function fetchInitialBoard() {
        fetch("/api/game/initialBoard")
            .then(response => response.json())
            .then(board => {
                renderBoard(board);
            })
            .catch(error => console.error("Error fetching initial board:", error));
    }

    function renderBoard(board) {
        chessboard.innerHTML = '';
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
                    square.appendChild(img);
                }
                chessboard.appendChild(square);
            });
        });

        addDragAndDropListeners();
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

    function handleDragStart(event) {
        const color = event.target.dataset.color;
        if ((isWhiteTurn && color === "white") || (!isWhiteTurn && color === "black")) {
            event.dataTransfer.setData("text/plain", JSON.stringify({
                startX: event.target.dataset.row,
                startY: event.target.dataset.col,
                piece: event.target.dataset.piece,
                color: event.target.dataset.color
            }));
        } else {
            event.preventDefault();
            alert("It's not your turn!");
        }
    }

    function handleDragOver(event) {
        event.preventDefault();
    }

    function handleDrop(event) {
        event.preventDefault();
        const startData = JSON.parse(event.dataTransfer.getData("text/plain"));
        const endX = event.target.dataset.row;
        const endY = event.target.dataset.col;

        if ((isWhiteTurn && startData.color === "white") || (!isWhiteTurn && startData.color === "black")) {
            const move = {
                startX: parseInt(startData.startX),
                startY: parseInt(startData.startY),
                endX: parseInt(endX),
                endY: parseInt(endY)
            };

            let moveUrl = "/api/game/move" + startData.piece;
            moveUrl = moveUrl.charAt(0).toLowerCase() + moveUrl.slice(1);  // Ensure URL starts with a lowercase letter

            fetch(moveUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(move)
            }).then(response => response.json())
                .then(result => {
                    if (result === true || result === "VALID_MOVE") {
                        updateBoardWithMove(move);
                        isWhiteTurn = !isWhiteTurn; // 切换回合
                    } else if (result === "BLACK_WINS") {
                        alert("Black wins!");
                        fetchInitialBoard(); // 重置初始棋盘
                    } else if (result === "WHITE_WINS") {
                        alert("White wins!");
                        fetchInitialBoard(); // 重置初始棋盘
                    } else if (result === "STALEMATE") {
                        alert("Stalemate!");
                        fetchInitialBoard(); // 重置初始棋盘
                    } else {
                        alert("Invalid move");
                    }
                });
        } else {
            alert("It's not your turn!");
        }
    }

    function updateBoardWithMove(move) {
        const startSquare = document.querySelector(`.square[data-row="${move.startX}"][data-col="${move.startY}"]`);
        const targetSquare = document.querySelector(`.square[data-row="${move.endX}"][data-col="${move.endY}"]`);

        // Remove any existing piece in the target square (to handle captures)
        if (targetSquare.firstChild) {
            targetSquare.removeChild(targetSquare.firstChild);
        }

        // Move the piece to the target square
        const piece = document.querySelector(`.piece[data-row="${move.startX}"][data-col="${move.startY}"]`);
        piece.dataset.row = move.endX;
        piece.dataset.col = move.endY;
        targetSquare.appendChild(piece);

        // Clear the start square
        startSquare.innerHTML = "";
    }

    fetchInitialBoard(); // Fetch and render initial board state from the backend
});
