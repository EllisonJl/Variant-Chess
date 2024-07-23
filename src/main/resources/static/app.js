document.addEventListener("DOMContentLoaded", function() {
    const chessboard = document.getElementById("chessboard");
    const ruleDisplay = document.getElementById("ruleDisplay");
    const restartButton = document.getElementById("restartButton");
    let isWhiteTurn = true;

    function fetchInitialBoard() {
        fetch("/api/game/initialBoard")
            .then(response => response.json())
            .then(board => {
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
            })
            .catch(error => console.error("Error fetching current rule:", error));
    }

    function renderBoard(board) {
        board.forEach((row, rowIndex) => {
            row.forEach((piece, colIndex) => {
                const square = document.createElement("div");
                square.classList.add("square");
                square.dataset.row = rowIndex;
                square.dataset.col = colIndex;

                if ((rowIndex + colIndex) % 2 === 0) {
                    square.classList.add("even");
                    square.style.backgroundColor = "#f0d9b5"; /* 浅色 */
                } else {
                    square.style.backgroundColor = "#b58863"; /* 深色 */
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
        addHoverListeners(); // 添加悬浮监听器
    }

    function addDragAndDropListeners() {
        chessboard.addEventListener("dragstart", handleDragStart);
        chessboard.addEventListener("dragover", handleDragOver);
        chessboard.addEventListener("drop", handleDrop);
    }

    function addHoverListeners() {
        chessboard.addEventListener('mouseover', handleMouseOver);
        chessboard.addEventListener('mouseout', handleMouseOut);
    }

    function handleDragStart(event) {
        if (event.target.classList.contains('piece')) {
            const piece = event.target;
            event.dataTransfer.setData("text/plain", JSON.stringify({
                startX: piece.dataset.row,
                startY: piece.dataset.col,
                piece: piece.dataset.piece,
                color: piece.dataset.color
            }));
        }
    }

    function handleDragOver(event) {
        event.preventDefault();
    }

    function handleDrop(event) {
        event.preventDefault();
        const startData = JSON.parse(event.dataTransfer.getData("text/plain"));
        const target = event.target.closest('.square');
        if (!target) return;

        const move = {
            startX: parseInt(startData.startX),
            startY: parseInt(startData.startY),
            endX: parseInt(target.dataset.row),
            endY: parseInt(target.dataset.col),
            piece: startData.piece,
            color: startData.color
        };

        if ((isWhiteTurn && move.color === "white") || (!isWhiteTurn && move.color === "black")) {
            validateAndMovePiece(move);
        } else {
            alert("It's not your turn!");
        }
    }

    function validateAndMovePiece(move) {
        let moveUrl = `/api/game/move${move.piece}`;
        moveUrl = moveUrl.charAt(0).toLowerCase() + moveUrl.slice(1);

        fetch(moveUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(move)
        }).then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error, status = ${response.status}`);
            }
            return response.text();
        })
            .then(result => {
                if (result === "VALID_MOVE") {
                    updateBoardWithMove(move);
                    isWhiteTurn = !isWhiteTurn;
                } else {
                    alert(result);
                }
            })
            .catch(error => console.error("Error processing move:", error));
    }

    function updateBoardWithMove(move) {
        const startSquare = document.querySelector(`.square[data-row="${move.startX}"][data-col="${move.startY}"]`);
        const targetSquare = document.querySelector(`.square[data-row="${move.endX}"][data-col="${move.endY}"]`);

        if (startSquare && targetSquare && startSquare.firstChild) {
            const piece = startSquare.firstChild;
            startSquare.removeChild(piece);
            targetSquare.appendChild(piece);
            piece.dataset.row = move.endX;
            piece.dataset.col = move.endY;
        }
    }

    function clearBoard() {
        while (chessboard.firstChild) {
            chessboard.removeChild(chessboard.firstChild);
        }
    }

    restartButton.addEventListener("click", function() {
        fetch("/api/game/restart", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(() => {
            clearBoard();
            fetchInitialBoard();
            fetchCurrentRule();
            isWhiteTurn = true;
        }).catch(error => console.error("Error restarting game:", error));
    });

    function handleMouseOver(event) {
        const piece = event.target.closest('.piece');
        if (piece) {
            console.log(`Mouse over piece: ${piece.dataset.piece} of color ${piece.dataset.color}`);
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
                    console.log(validMoves);
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

    fetchInitialBoard();
    fetchCurrentRule();
});


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