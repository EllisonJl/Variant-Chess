document.addEventListener("DOMContentLoaded", function() {
    const chessboard = document.getElementById("chessboard");
    let isWhiteTurn = true; // 白棋先行

    function initializeBoard() {
        const board = Array(8).fill(null).map(() => Array(8).fill(null));

        const majorPieces = ["Knight", "Knight", "Bishop", "Bishop", "Queen", "King"];
        shuffleArray(majorPieces);

        board[0] = ["Rook", ...majorPieces, "Rook"].map(type => ({ type, color: "Black" }));
        board[7] = ["Rook", ...majorPieces, "Rook"].map(type => ({ type, color: "White" }));

        const pawns = ["Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn"];
        shuffleArray(pawns);

        board[1] = [pawns[0], "Cannon", pawns[1], pawns[2], pawns[3], pawns[4], "Cannon", pawns[5]].map(type => ({ type, color: "Black" }));
        board[6] = [pawns[0], "Cannon", pawns[1], pawns[2], pawns[3], pawns[4], "Cannon", pawns[5]].map(type => ({ type, color: "White" }));

        return board;
    }

    function shuffleArray(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
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
                    img.src = `images/${piece.color}${piece.type}.png`;
                    img.classList.add("piece");
                    img.draggable = true;
                    img.dataset.row = rowIndex;
                    img.dataset.col = colIndex;
                    img.dataset.piece = piece.type;
                    img.dataset.color = piece.color;
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
        if ((isWhiteTurn && color === "White") || (!isWhiteTurn && color === "Black")) {
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

        if ((isWhiteTurn && startData.color === "White") || (!isWhiteTurn && startData.color === "Black")) {
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
                        renderBoard(updateBoardWithMove(initialBoard, move));
                        isWhiteTurn = !isWhiteTurn; // 切换回合
                    } else if (result === "BLACK_WINS") {
                        alert("Black wins!");
                        const newBoard = initializeBoard();
                        renderBoard(newBoard);
                        initialBoard = newBoard; // 重置初始棋盘
                    } else if (result === "WHITE_WINS") {
                        alert("White wins!");
                        const newBoard = initializeBoard();
                        renderBoard(newBoard);
                        initialBoard = newBoard; // 重置初始棋盘
                    } else if (result === "STALEMATE") {
                        alert("Stalemate!");
                        const newBoard = initializeBoard();
                        renderBoard(newBoard);
                        initialBoard = newBoard; // 重置初始棋盘
                    } else {
                        alert("Invalid move");
                    }
                });
        } else {
            alert("It's not your turn!");
        }
    }

    function updateBoardWithMove(board, move) {
        const piece = board[move.startX][move.startY];
        board[move.startX][move.startY] = null;
        board[move.endX][move.endY] = piece;
        return board;
    }

    let initialBoard = initializeBoard();
    renderBoard(initialBoard);
});