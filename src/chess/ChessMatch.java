package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	private int turn;
	private Color currentPlayer;
	private Board board;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
    public int getTurn(){
    	return turn;
    
    }
    public Color getCurrentPlayer(){
    	return currentPlayer;
    }
	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}

		}
		return mat;
	}
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

    public ChessPiece PeformChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
    	Position source = sourcePosition.toPosition();
    	Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source,target);
        nextTurn();
        return (ChessPiece)capturedPiece;
    }
    private Piece makeMove(Position source, Position target){
    	Piece p = board.RemovePiece(source);
    	Piece capturedPiece = board.RemovePiece(target);
    	board.PlacePiece(p, target);
    	
    	if (capturedPiece != null) {
    		piecesOnTheBoard.remove(capturedPiece);
    		capturedPieces.add(capturedPiece);
    	}
    	return capturedPiece;
    }
    
    private void validateSourcePosition(Position position) {
    	if(!board.ThereIsAPiece(position)) {
    		throw new ChessException("There is no piece on source position");
    	}
    	if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
    		throw new ChessException("The chosen piece is not yours");
    	}
    	if(!board.piece(position).isThereAnyPossibleMove()) {
    		throw new ChessException("There is no possible moves for the chosen piece");
    	}
    }
    private void validateTargetPosition(Position source, Position target) {
    	if(!board.piece(source).possibleMove(target)) {
    		throw new ChessException("The chosen piece can't move to target position");
    	}
    }
    private void nextTurn() {
    	turn++;
    	currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
	private void PlaceNewPiece(char column, int row, ChessPiece piece) {
		board.PlacePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	private void initialSetup() {
		PlaceNewPiece('c', 1, new Rook(board, Color.WHITE));
        PlaceNewPiece('c', 2, new Rook(board, Color.WHITE));
        PlaceNewPiece('d', 2, new Rook(board, Color.WHITE));
        PlaceNewPiece('e', 2, new Rook(board, Color.WHITE));
        PlaceNewPiece('e', 1, new Rook(board, Color.WHITE));
        PlaceNewPiece('d', 1, new King(board, Color.WHITE));

        PlaceNewPiece('c', 7, new Rook(board, Color.BLACK));
        PlaceNewPiece('c', 8, new Rook(board, Color.BLACK));
        PlaceNewPiece('d', 7, new Rook(board, Color.BLACK));
        PlaceNewPiece('e', 7, new Rook(board, Color.BLACK));
        PlaceNewPiece('e', 8, new Rook(board, Color.BLACK));
        PlaceNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
