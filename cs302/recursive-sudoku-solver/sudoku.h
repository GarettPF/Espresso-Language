#ifndef SUDOKU_H
#define SUDOKU_H

#include <iostream>
#include <fstream>

using namespace std;

/**
 * @brief			This function loads in the board from a .txt file
 * @param board		The 2D array that stores the board state
 * @param input		The input file that contains the user given puzzle
*/
void load_puzzle(int board[][9], ifstream& input);

/**
 * @brief			Print the sudoku puzzle in a nice format
 * @param board		The current sudoku puzzle to print out
*/
void print_puzzle(int board[][9]);

/**
 * @brief			checks the current position of the board to see if the @param num
 *					will work in that current state of the board
 * @param board		The board to check
 * @param row		The row of the position
 * @param col		The column of the position
 * @param num		The number to check if valid
 * @return			True if possible, False otherwise
*/
bool check_position(int board[][9], int row, int col, int num);

/**
 * @brief			The recursive function that will solve the given sudoku puzzle
 * @param board		The current state of the board to solve
 * @param row		The current row of the position
 * @param col		The current col of the position
 * @return			True if solved; False otherwise
*/
bool solve_puzzle(int board[][9], int row, int col);



void load_puzzle(int board[][9], ifstream& input) {
	for (int y = 0; y < 9; y++) {
		for (int x = 0; x < 9; x++) {
			input >> board[y][x];
		}
	}
}



void print_puzzle(int board[][9]) {
	cout << "-------------------------------" << endl;
	for (int y = 0; y < 9; y++) {
		if ((y-2) % 3 == 1)
			cout << "-------------------------------" << endl;
		for (int x = 0; x < 9; x++) {
			if (x % 3 == 0)
				cout << "|";
			if (board[y][x] == 0)
				cout << " - ";
			else
				cout << " " << board[y][x] << " ";
		}
		cout << "|" << endl;
	}
	cout << "-------------------------------" << endl;
}



bool check_position(int board[][9], int row, int col, int num) {
	for (int x = 0; x < 9; x++) { // check the current row
		if (board[row][x] == num)
			return false;
	}
	for (int y = 0; y < 9; y++) { // check the current col
		if (board[y][col] == num)
			return false;
	}

	// ex. Section 1 is X=0, Y=0; Section 9 is X=2, Y=2;
	int sectionX = (int)col/3, 
		sectionY = (int)row/3; 
	
	for (int x = 0; x < 3; x++) { // check the current section
		for (int y = 0; y < 3; y++) {
			if (board[(sectionY * 3) + y][(sectionX * 3) + x] == num)
				return false;
		}
	}

	// if passed all 3 tests, the number currently works
	return true;
}



bool solve_puzzle(int board[][9], int row, int col) {
	int _row, _col;


	// check if puzzle is solved
	if (row == 9)
		return true;

	// calculate the next position
	_row = (col == 8) ? row + 1 : row;
	_col = (col == 8) ? 0 : col + 1;

	// check if on non blank position
 	if (board[row][col] != 0) {
		return (solve_puzzle(board, _row, _col));
	} else { // position is blank
		for (int num = 1; num <= 9; num++) {
			if (check_position(board, row, col, num)) {
				board[row][col] = num;
				if (solve_puzzle(board, _row, _col)) // puzzle is solved
					return true;
			}
		}
		board[row][col] = 0;
		return false;
	}
}


#endif