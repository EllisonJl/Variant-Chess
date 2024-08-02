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
                    img.draggable = true; //设置为可以进行拖动
                    img.dataset.row = rowIndex;
                    img.dataset.col = colIndex;
                    img.dataset.piece = piece.type;
                    img.dataset.color = piece.color.toLowerCase();
                    img.dataset.captureCount = piece.captureCount || 0;
                    img.dataset.isFirstMove = piece.isFirstMove || "true"; // 添加首次移动状态
                    if (piece.immobile) {
                        img.classList.add('immobile');
                    }
                    square.appendChild(img);
                }
                chessboard.appendChild(square);
            });
        });

        addDragAndDropListeners(); //在棋盘的每个格子上添加监听器，允许用户拖动棋子，并把它放到新的位置上
        addHoverListeners(); //当用户的鼠标悬停在棋子上时，可以在棋盘上高亮出棋子可以走的路径
    }

    function addDragAndDropListeners() {
        const pieces = document.querySelectorAll(".piece");  //获取页面上所有带piece类的棋子
        pieces.forEach(piece => {
            piece.addEventListener("dragstart", handleDragStart);
        });

        const squares = document.querySelectorAll(".square");
        squares.forEach(square => {
            square.addEventListener("dragover", handleDragOver); //当用户拖动棋子的时候会触发handleDragOver函数
            square.addEventListener("drop", handleDrop);//当用户将棋子放置在棋盘格子上时会触发handleDrop函数
        });
    }

    function addHoverListeners() {
        chessboard.addEventListener('mouseover', handleMouseOver); //鼠标放在棋盘上时可以高亮显示出棋子的路径
        chessboard.addEventListener('mouseout', handleMouseOut);//鼠标离开棋盘的时候会清除高亮显示
    }

    function handleMouseOver(event) {
        const piece = event.target.closest('.piece');
        if (piece) {
            const startX = parseInt(piece.dataset.row); //获取棋子的行号
            const startY = parseInt(piece.dataset.col);//获取棋子的列号
            const color = piece.dataset.color.toUpperCase();//获取棋子的颜色并转化为大写
            const pieceType = piece.dataset.piece;//获取棋子的类型
            //创建一个payload对象，里面包含了对象的横坐标和纵坐标，棋子的类型以及颜色
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
                body: JSON.stringify(payload) //将payload转化为json字符串进行发送
            })
                //这个代码是处理响应的代码
                .then(response => {
                    //如果response是false的话就抛出一个错误
                    if (!response.ok) {
                        throw new Error(`HTTP error, status = ${response.status}`);
                    }
                    return response.json();
                })
                //处理response.json的数据
                .then(validMoves => {
                    //检查validMoves是否是一个数组
                    if (Array.isArray(validMoves)) {
                        //高亮显示出合法移动的位置
                        highlightValidMoves(piece, validMoves);
                    }
                    //如果数据不符合格式的要求，就显示错误的信息出来
                    else {
                        console.error('Expected an array of valid moves, got:', validMoves);
                    }
                })
                //捕获前面的then中出现的错误
                .catch(error => {
                    console.error("Error fetching valid moves:", error);
                });
        }
    }

    function handleMouseOut(event) {
        clearHighlights();
    }

    function highlightValidMoves(piece, validMoves) {
        //调用clearHighlights函数清除棋盘上所有的高亮显示的方格
        clearHighlights();
        //validMoves是一个数组，这个代码的作用是依次遍历这个数组，每一个数组里面包含了目标位置的行和列。然后对每一个合法的位置进行处理
        validMoves.forEach(move => {
            //根据目标位置的行和列选择棋盘上的方格元素。
            const targetSquare = document.querySelector(`.square[data-row="${move.endX}"][data-col="${move.endY}"]`);
            if (targetSquare) {
                //检查目标是否为浅色方格上的，如果是的话就添加highlight-move类
                targetSquare.classList.add('highlight-move');
                //检查目标是否为浅色方格上的，如果是的话就添加even类
                if ((move.endX + move.endY) % 2 === 0) {
                    targetSquare.classList.add('even');
                }
            }
        });
    }

    function clearHighlights() {
        //选择所有高亮显示的方格
        const highlightedSquares = document.querySelectorAll('.highlight-move');
        //对高亮显示的方格进行遍历
        highlightedSquares.forEach(square => {
            square.classList.remove('highlight-move');
            square.classList.remove('even');
        });
    }

    function handleDragStart(event) {
        //获取当前被拖动的棋子的颜色
        const color = event.target.dataset.color;
        //逻辑可能存在问题
        if ((isWhiteTurn && color === "white") || (!isWhiteTurn && color === "black")) {
            event.dataTransfer.setData("text/plain", JSON.stringify({
                startX: event.target.dataset.row,
                startY: event.target.dataset.col,
                piece: event.target.dataset.piece,
                color: event.target.dataset.color,
                captureCount: event.target.dataset.captureCount,
                isFirstMove: event.target.dataset.isFirstMove // 传递首次移动状态
            }));
        } else {
            event.preventDefault();
            alert("It's not your turn!");
        }
    }

    //不明白
    function handleDragOver(event) {
        event.preventDefault();
    }

    function handleDrop(event) {
        event.preventDefault();
        const startData = JSON.parse(event.dataTransfer.getData("text/plain"));
        //查找最近的square元素
        const target = event.target.closest('.square');
        //如果没有找到目标元素，就退出handledrop函数
        if (!target) return;

        //创建一个包含移动信息的对象
        const move = {
            startX: parseInt(startData.startX),
            startY: parseInt(startData.startY),
            endX: parseInt(target.dataset.row),
            endY: parseInt(target.dataset.col),
            piece: startData.piece,
            color: startData.color
        };

        //逻辑可能有问题
        if ((isWhiteTurn && move.color === "white") || (!isWhiteTurn && move.color === "black")) {
            validateAndMovePiece(move);
        } else {
            alert("It's not your turn!");
        }
    }

    function validateAndMovePiece(move) {

        console.log("Piece being moved:", move.piece); // 打印 move.piece 的值
        let moveUrl = `/api/game/move${move.piece}`;

        moveUrl = moveUrl.charAt(0).toLowerCase() + moveUrl.slice(1);
        //使用 fetch API 发送 POST 请求，将移动数据发送到服务器
        fetch(moveUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(move)
            //处理响应
        }).then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error, status = ${response.status}`);
            }
            return response.text();
        })

            .then(result => {
                //如果移动有效的话，更新棋盘
                if (result === "VALID_MOVE") {
                    //这个updateBoardMove可能有问题
                    //调用updateBoardWithMove更新棋子的新位置和状态
                    updateBoardWithMove(move);
                    //切换来回
                    isWhiteTurn = !isWhiteTurn;
                } else if (result === "BLACK_WINS") {
                    alert("Black wins!");
                    //为什么需要调用这两个函数
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
            // 捕获目标方格中的棋子
            const capturedPiece = targetSquare.firstChild;
            //找到指定位置的棋子元素，即在
            const piece = document.querySelector(`.piece[data-row="${move.startX}"][data-col="${move.startY}"]`);
            //获取指定位置的棋子的类型
            const pieceType = piece.dataset.piece;

            if (pieceType === 'King' || pieceType === 'Queen') {
                capturedPiece.dataset.color = piece.dataset.color;
                capturedPiece.src = `images/${capturedPiece.dataset.color}${capturedPiece.dataset.piece}.png`;
                // capturedPiece.classList.add('immobile');
                //计算king和queen捕获棋子之后落到前一格的坐标
                const deltaX = move.endX - move.startX;
                const deltaY = move.endY - move.startY;
                const newX = move.endX - Math.sign(deltaX);
                const newY = move.endY - Math.sign(deltaY);

                const newSquare = document.querySelector(`.square[data-row="${newX}"][data-col="${newY}"]`);

                if (newSquare.firstChild) {
                    //逻辑有问题，如果前一格有棋子的话就移除棋子，但是如果前一格是king或者queen的话根本不需要移除，不然的话king和queen不就又可以再一次把捕获的棋子转化为己方棋子了吗
                    newSquare.removeChild(newSquare.firstChild);
                }
                piece.dataset.row = newX; // 设置新行位置
                piece.dataset.col = newY; // 设置新列位置
                newSquare.appendChild(piece); // 将白色 Queen 添加到新的方格，但是逻辑有问题，没有转换棋子的颜色

                targetSquare.innerHTML = ""; // 清空目标方格 (4, 4)
                targetSquare.appendChild(capturedPiece); // 将捕获后的棋子（现在为白色 Pawn）放置在目标方格

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
            //依次遍历四个方向
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
            clearBoard(); // Clear all pieces from the chessboard
            fetchInitialBoard(); // Fetch and render initial board state from the backend
            fetchCurrentRule(); // Fetch and display the current rule
            isWhiteTurn = true; // Reset to white's turn
        }).catch(error => console.error("Error restarting game:", error));
    });

    fetchInitialBoard();
    fetchCurrentRule();
});
