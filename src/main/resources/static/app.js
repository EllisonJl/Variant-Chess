document.addEventListener("DOMContentLoaded", function() {
    const chessboard = document.getElementById("chessboard");

    function initializeBoard() {
        const board = Array(8).fill(null).map(() => Array(8).fill(""));

        const majorPieces = ["Knight", "Knight", "Bishop", "Bishop", "King", "Queen"];
        shuffleArray(majorPieces);
        board[7] = ["Rook", ...majorPieces.slice(0, 3), ...majorPieces.slice(3), "Rook"];
        board[0] = board[7].slice();

        const pawns = ["Pawn", "Pawn", "Pawn", "Pawn", "Pawn", "Pawn"];
        shuffleArray(pawns);
        board[6] = [pawns[0], "Cannon", pawns[1], pawns[2], pawns[3], pawns[4], "Cannon", pawns[5]];
        board[1] = [pawns[0], "Cannon", pawns[1], pawns[2], pawns[3], pawns[4], "Cannon", pawns[5]];

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
                    img.src = `images/${(rowIndex >= 6 ? "White" : "Black") + piece}.png`;
                    img.classList.add("piece");
                    img.draggable = true;
                    img.dataset.row = rowIndex;
                    img.dataset.col = colIndex;
                    img.dataset.piece = piece;
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
        event.dataTransfer.setData("text/plain", JSON.stringify({
            startX: event.target.dataset.row,
            startY: event.target.dataset.col,
            piece: event.target.dataset.piece
        }));
    }

    function handleDragOver(event) {
        event.preventDefault();
    }

    function handleDrop(event) {
        event.preventDefault();
        const startData = JSON.parse(event.dataTransfer.getData("text/plain"));
        const endX = event.target.dataset.row;
        const endY = event.target.dataset.col;

        const move = {
            startX: parseInt(startData.startX),
            startY: parseInt(startData.startY),
            endX: parseInt(endX),
            endY: parseInt(endY)
        };

        let moveUrl = "/api/game/move";
        if (startData.piece === "Pawn") {
            moveUrl = "/api/game/movePawn";
        } else if (startData.piece === "Cannon") {
            moveUrl = "/api/game/moveCannon";
        }

        fetch(moveUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(move)
        }).then(response => response.json())
            .then(isValid => {
                if (isValid) {
                    renderBoard(updateBoardWithMove(initialBoard, move));
                } else {
                    alert("Invalid move");
                }
            });
    }

    function updateBoardWithMove(board, move) {
        const piece = board[move.startX][move.startY];
        board[move.startX][move.startY] = "";
        board[move.endX][move.endY] = piece;
        return board;
    }

    const initialBoard = initializeBoard();
    renderBoard(initialBoard);
});
