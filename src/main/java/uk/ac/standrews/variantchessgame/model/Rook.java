            return false;
        }

        // Check if the move is either vertical or horizontal
        if (startX != endX && startY != endY) {
            return false;
        }

        // Determine the step direction for the move
        int stepX = Integer.compare(endX - startX, 0);
        int stepY = Integer.compare(endY - startY, 0);

        // Check for any pieces blocking the path
        for (int i = 1; i < Math.max(Math.abs(endX - startX), Math.abs(endY - startY)); i++) {
            if (board.getPieceAt(startX + i * stepX, startY + i * stepY) != null) {
                return false;
            }
        }

        // Check if the target position contains a piece of the same color
        VariantChessPiece targetPiece = board.getPieceAt(endX, endY);
        if (targetPiece != null && targetPiece.getColor() == this.getColor()) {
            return false;
        }

        // If the target position contains an opponent's piece, mark the move as a capture
        if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
            move.setCapture(true);
        }

        return true;
    }
}
