function updateBoardWithMove(move) {
    const startSquare = document.querySelector(`.square[data-row="${move.startX}"][data-col="${move.startY}"]`);
    const targetSquare = document.querySelector(`.square[data-row="${move.endX}"][data-col="${move.endY}"]`);

    // 检查是否有捕获
    const isCapture = targetSquare.firstChild !== null;

    // 如果有捕获并且是King或者Queen，更新棋盘状态
    if (isCapture) {
        const capturedPiece = targetSquare.firstChild;
        const piece = document.querySelector(`.piece[data-row="${move.startX}"][data-col="${move.startY}"]`);
        const pieceType = piece.dataset.piece;

        if (pieceType === 'King' || pieceType === 'Queen') {
            // 更新被捕获棋子的颜色
            capturedPiece.dataset.color = piece.dataset.color;
            capturedPiece.src = `images/${capturedPiece.dataset.color}${capturedPiece.dataset.piece}.png`;
            capturedPiece.classList.add('immobile');  // Mark as immobile visually

            // 移动King或Queen到前一格
            const deltaX = move.endX - move.startX;
            const deltaY = move.endY - move.startY;
            const newX = move.endX - Math.sign(deltaX);
            const newY = move.endY - Math.sign(deltaY);

            // 确保前一格没有其他棋子
            const newSquare = document.querySelector(`.square[data-row="${newX}"][data-col="${newY}"]`);
            if (newSquare.firstChild) {
                newSquare.removeChild(newSquare.firstChild);
            }
            piece.dataset.row = newX;
            piece.dataset.col = newY;
            newSquare.appendChild(piece);

            // 更新捕获格子为被捕获的棋子
            targetSquare.innerHTML = "";
            targetSquare.appendChild(capturedPiece);
        } else {
            // 正常捕获逻辑，移除被捕获的棋子
            targetSquare.removeChild(capturedPiece);
            piece.dataset.row = move.endX;
            piece.dataset.col = move.endY;
            targetSquare.appendChild(piece);
        }
    } else {
        // 移动棋子到目标格子
        const piece = document.querySelector(`.piece[data-row="${move.startX}"][data-col="${move.startY}"]`);
        piece.dataset.row = move.endX;
        piece.dataset.col = move.endY;
        targetSquare.appendChild(piece);
    }

    // 清空起始格子
    startSquare.innerHTML = "";

    // 检查炮是否发生爆炸
    const piece = document.querySelector(`.piece[data-row="${move.endX}"][data-col="${move.endY}"]`);
    if (piece.dataset.piece === 'Cannon' && parseInt(piece.dataset.captureCount) >= 3) {
        const directions = [
            {x: 1, y: 0}, {x: -1, y: 0}, {x: 0, y: 1}, {x: 0, y: -1}
        ];
        directions.forEach(dir => {
            const x = move.endX + dir.x;
            const y = move.endY + dir.y;
            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                const adjacentSquare = document.querySelector(`.square[data-row="${x}"][data-col="${y}"]`);
                const adjacentPiece = adjacentSquare.firstChild;
                if (adjacentPiece && adjacentPiece.dataset.color !== piece.dataset.color) {
                    adjacentSquare.removeChild(adjacentPiece);
                }
            }
        });
        // 移除自爆的炮
        targetSquare.removeChild(piece);
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const chessboard = document.getElementById("chessboard");
    const ruleDisplay = document.getElementById("ruleDisplay"); // 添加一个显示规则的元素
    let isWhiteTurn = true; // 白棋先行

    function fetchInitialBoard() {
        fetch("/api/game/initialBoard")
            .then(response => response.json())
            .then(board => {
                renderBoard(board);
            })
            .catch(error => console.error("Error fetching initial board:", error));
    }

    function fetchCurrentRule() {
        fetch("/api/game/currentRule")
            .then(response => response.text())
            .then(rule => {
                ruleDisplay.textContent = `当前规则: ${rule}`;
            })
            .catch(error => console.error("Error fetching current rule:", error));
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
                    img.dataset.captureCount = piece.captureCount;  // Add capture count
                    if (piece.immobile) {
                        img.classList.add('immobile');  // Add class for immobile pieces
                    }
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
                color: event.target.dataset.color,
                captureCount: event.target.dataset.captureCount  // Include capture count
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
            }).then(response => response.text()) // Change to text() to handle response strings
                .then(result => {
                    if (result === "VALID_MOVE") {
                        updateBoardWithMove(move);
                        isWhiteTurn = !isWhiteTurn; // 切换回合
                    } else if (result === "BLACK_WINS") {
                        alert("Black wins!");
                        fetchInitialBoard(); // 重置初始棋盘
                        fetchCurrentRule(); // 获取并显示当前规则
                    } else if (result === "WHITE_WINS") {
                        alert("White wins!");
                        fetchInitialBoard(); // 重置初始棋盘
                        fetchCurrentRule(); // 获取并显示当前规则
                    } else if (result === "STALEMATE") {
                        alert("Stalemate!");
                        fetchInitialBoard(); // 重置初始棋盘
                        fetchCurrentRule(); // 获取并显示当前规则
                    } else {
                        alert("Invalid move");
                    }
                })
                .catch(error => console.error("Error processing move:", error));
        } else {
            alert("It's not your turn!");
        }
    }

    fetchInitialBoard(); // Fetch and render initial board state from the backend
    fetchCurrentRule(); // Fetch and display the current rule
});
