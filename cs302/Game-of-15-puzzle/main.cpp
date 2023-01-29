/*
	Name: Garett Pascual-Folster, #2001691416, Assignment 1
	Description: recreation of the game 15 puzzle using the
		knowledge of linked list and other high-level programming
		skills.
	Input: Requires a text file to load in a puzzle, using the keys U, D, L, R
		to control the game board and move the tiles.
	Output: A visual of the game board and each number representing the game.
*/



#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include "LinkedList.h"

using namespace std;

int main() {
	ifstream input;
	string filename;
	LL<int>::iterator space;
	bool play_again = true;

	while (play_again) {
		bool finished = false, winner = false;

		do { // ask for puzzle file
			cout << "Enter puzzle file: ";
			cin >> filename;
			input.open(filename.c_str());
		} while (!input.is_open());

		// load in board from text file
		LL<int> board[4];
		int value = 0;
		string line;
		for (int y = 0; y < 4; y++) {
			getline(input, line);
			stringstream linestream(line);
			for (int x = 0; x < 4; x++) {
				linestream >> value;
				board[y].tailInsert(value);
			}
		}

		// main game loop
		while (!finished) {

			// display board neatly
			LL<int>::iterator it;
			int space_y;
			cout << endl;
			for (int y = 0; y < 4; y++) {
				it = board[y].begin();
				cout << "   ";
				for (int x = 0; x < 4; x++) {
					cout << "  ";
					if (*it == 0) {
						cout << " -";
						space = it;
						space_y = y;
					} else if (*it < 10)
						cout << " " << *it;
					else
						cout << *it;
					it++;
				}
				cout << endl;
			}
			cout << endl;

			// ask user for input
			char choice;
			string choices = "LlRrUuDdQq";
			do {
				cout << "Please make your choice, (L)eft, (R)ight, (U)p, (D)own, (Q)uit: ";
				cin >> choice;
				if (choices.find(choice) == string::npos)
					cout << endl << "Invalid selection!" << endl << endl;
			} while (choices.find(choice) == string::npos);

			// do the action the user chose
			if (choice == 'Q' || choice == 'q') { // quiting the game
				finished = true;
			} else if (choice == 'L' || choice == 'l') { // moving to the left
				LL<int>::iterator right = space;
				right++;
				if (right != nullptr) {
					int right_value = *right;
					board[space_y].update(right, 0);
					board[space_y].update(space, right_value);
				}
			} else if (choice == 'R' || choice == 'r') { // moving to the right
				LL<int>::iterator left = space;
				left--;
				if (left != nullptr) {
					int left_value = *left;
					board[space_y].update(space, left_value);
					board[space_y].update(left, 0);
				}
			} else if (choice == 'U' || choice == 'u') { // moving up
				if (space_y != 3) {
					LL<int>::iterator down = board[space_y+1].begin();
					LL<int>::iterator check = board[space_y].begin();
					while (check != space) {
						check++;
						down++;
					}
					int down_value = *down;
					board[space_y].update(space, down_value);
					board[space_y+1].update(down, 0);
				}
			} else if (choice == 'D' || choice == 'd') { // moving down
				if (space_y != 0) {
					LL<int>::iterator up = board[space_y-1].begin();
					LL<int>::iterator check = board[space_y].begin();
					while (check != space) {
						check++;
						up++;
					}
					int up_value = *up;
					board[space_y].update(space, up_value);
					board[space_y-1].update(up, 0);
				}
			}

			// check if the game is complete and the user is a winner
			int check = 1;
			bool flag = true;
			for (int y = 0; y <= 3; y++) {
				LL<int>::iterator it = board[y].begin();
				for (int x = 0; x <= 3; x++) {
					if (*it != check && check != 16)
						flag = false;
					it++;
					check++;
				}
			}
			winner = flag;
			if (flag) finished = true;
		}

		if (!winner) {
			cout << "Quitter..." << endl;
			play_again = false;
		} else if (winner) {
			char choice;

			// display board neatly
			cout << endl;
			LL<int>::iterator it;
			int space_y;
			for (int y = 0; y < 4; y++) {
				it = board[y].begin();
				cout << "   ";
				for (int x = 0; x < 4; x++) {
					cout << "  ";
					if (*it == 0) {
						cout << " -";
						space = it;
						space_y = y;
					}
					else if (*it < 10)
						cout << " " << *it;
					else
						cout << *it;
					it++;
				}
				cout << endl;
			}
			cout << endl;
			cout << "You win!!!" << endl;
			cout << "Play again? (Y/N): ";
			cin >> choice;
			if (choice == 'N' || choice == 'n') 
				play_again = false;
			else if (choice == 'Y' || choice == 'y')
				play_again = true;
		}
		input.close();
	}
}
