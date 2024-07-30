document.addEventListener("DOMContentLoaded", function() {
    const chessboard = document.getElementById("chessboard");
    const ruleDisplay = document.getElementById("ruleDisplay");
    const specificRuleDisplay = document.getElementById("specificRuleDisplay");
    const restartButton = document.getElementById("restartButton");
    let isWhiteTurn = true;

    function fetchInitialBoard() {
        fetch("/api/game/initialBoard")
            .then(response => response.json())
            .then(board => {
                console.log("Fetched initial board:", JSON.stringify(board));
                clearBoard(); // 确保在渲染前清空棋盘
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
                    img.draggable = true; // 设置为可以进行拖动
                    img.dataset.row = rowIndex;
                    img.dataset.col = colIndex;
                    img.dataset.piece = piece.type;
                    img.dataset.color = piece.color.toLowerCase();
                    img.dataset.captureCount = piece.captureCount || 0;
                    img.dataset.isFirstMove = piece.isFirstMove || "true"; // 添加首次移动状态
                    square.appendChild(img);
                }
                chessboard.appendChild(square);

            });
        });

        addDragAndDropListeners(); // 在棋盘的每个格子上添加监听器，允许用户拖动棋子，并把它放到新的位置上
        addHoverListeners(); // 当用户的鼠标悬停在棋子上时，可以在棋盘上高亮出棋子可以走的路径
    }

    function addDragAndDropListeners() {
        const pieces = document.querySelectorAll(".piece");  // 获取页面上所有带piece类的棋子
        pieces.forEach(piece => {
            piece.addEventListener("dragstart", handleDragStart);
        });

        const squares = document.querySelectorAll(".square");
        squares.forEach(square => {
            square.addEventListener("dragover", handleDragOver); // 当用户拖动棋子的时候会触发handleDragOver函数
            square.addEventListener("drop", handleDrop); // 当用户将棋子放置在棋盘格子上时会触发handleDrop函数
        });
    }

    function addHoverListeners() {
        chessboard.addEventListener('mouseover', handleMouseOver); // 鼠标放在棋盘上时可以高亮显示出棋子的路径
        chessboard.addEventListener('mouseout', handleMouseOut); // 鼠标离开棋盘的时候会清除高亮显示
    }

    function handleMouseOver(event) {
        const piece = event.target.closest('.piece');
        if (piece) {
            const startX = parseInt(piece.dataset.row); // 获取棋子的行号
            const startY = parseInt(piece.dataset.col); // 获取棋子的列号
            const color = piece.dataset.color.toUpperCase(); // 获取棋子的颜色并转化为大写
            const pieceType = piece.dataset.piece; // 获取棋子的类型
            const isFirstMove = piece.dataset.isFirstMove === "true"; // 获取首次移动状态

            const payload = {
                startX: startX,
                startY: startY,
                piece: pieceType,
                color: color,
                isFirstMove: isFirstMove // 添加首次移动状态到payload
            };

            fetch("/api/game/validMoves", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload) // 将payload转化为json字符串进行发送
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
            event.dataTransfer.setData("text/plain", JSON.stringify({
                startX: event.target.dataset.row,
                startY: event.target.dataset.col,
                piece: event.target.dataset.piece,
                color: event.target.dataset.color,
                captureCount: event.target.dataset.captureCount,
                isFirstMove: event.target.dataset.isFirstMove === "true" // 添加首次移动状态
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
        const target = event.target.closest('.square');
        if (!target) return;

        const move = {
            startX: parseInt(startData.startX),
            startY: parseInt(startData.startY),
            endX: parseInt(target.dataset.row),
            endY: parseInt(target.dataset.col),
            piece: startData.piece,
            color: startData.color,
            isFirstMove: startData.isFirstMove
        };

        if ((isWhiteTurn && move.color === "white") || (!isWhiteTurn && move.color === "black")) {
            validateAndMovePiece(move);
        } else {
            alert("It's not your turn!");
        }
    }

    function validateAndMovePiece(move) {
        console.log("Piece being moved:", move.piece);
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
                } else if (result === "BLACK_WINS") {
                    alert("Black wins!");
                    fetchInitialBoard();
                    fetchCurrentRule();
                } else if (result === "WHITE_WINS") {
                    alert("White wins!");
                    fetchInitialBoard();
                    fetchCurrentRule();
                } else if (result === "STALEMATE") {
                    alert("Stalemate!");
                    fetchInitialBoard();
                    fetchCurrentRule();
                } else {
                    alert("Invalid move");
                }
            })
            .catch(error => console.error("Error processing move:", error));
    }

    function updateBoardWithMove(move) {
        const startSquare = document.querySelector(`.square[data-row="${move.startX}"][data-col="${move.startY}"]`);
        const targetSquare = document.querySelector(`.square[data-row="${move.endX}"][data-col="${move.endY}"]`);

        const isCapture = targetSquare.firstChild !== null;

        if (isCapture) {
            const capturedPiece = targetSquare.firstChild;
            const piece = document.querySelector(`.piece[data-row="${move.startX}"][data-col="${move.startY}"]`);
            const pieceType = piece.dataset.piece;

            if (pieceType === 'King' || pieceType === 'Queen') {
                capturedPiece.dataset.color = piece.dataset.color;
                capturedPiece.src = `images/${capturedPiece.dataset.color}${capturedPiece.dataset.piece}.png`;

                const deltaX = move.endX - move.startX;
                const deltaY = move.endY - move.startY;
                const newX = move.endX - Math.sign(deltaX);
                const newY = move.endY - Math.sign(deltaY);

                const newSquare = document.querySelector(`.square[data-row="${newX}"][data-col="${newY}"]`);

                if (newSquare.firstChild) {
                    newSquare.removeChild(newSquare.firstChild);
                }
                piece.dataset.row = newX;
                piece.dataset.col = newY;
                newSquare.appendChild(piece);

                targetSquare.innerHTML = "";
                targetSquare.appendChild(capturedPiece);
            } else {
                targetSquare.removeChild(capturedPiece);
                piece.dataset.row = move.endX;
                piece.dataset.col = move.endY;
                targetSquare.appendChild(piece);
            }
        } else {
            const piece = document.querySelector(`.piece[data-row="${move.startX}"][data-col="${move.startY}"]`);
            piece.dataset.row = move.endX;
            piece.dataset.col = move.endY;
            targetSquare.appendChild(piece);
        }

        startSquare.innerHTML = "";

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
            targetSquare.removeChild(piece);
        }
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
            clearBoard(); // 清空棋盘
            fetchInitialBoard(); // 获取并渲染初始棋盘状态
            fetchCurrentRule(); // 获取并显示当前规则
            isWhiteTurn = true; // 重置为白方回合
        }).catch(error => console.error("Error restarting game:", error));
    });

    fetchInitialBoard();
    fetchCurrentRule();
});
