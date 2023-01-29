/**********************
*
*   Name: Garett Pascual-Folster, 2001691416, Assignment 5
*   Description: A simple LISP interpretation within C++
*   Input: an input file, with test cases
*   Output: for each test case, an output if the solution is possible or not. if so
*			output the integer value, otherwise so no solution.
*
**********************/


#include <cstdlib>
#include <iostream>
#include <fstream>
#include <string>
#include "myStack.h"
#include "binaryTree.h"
using namespace std;


int main() {
	ifstream file;
	string fileName;
	
	// get input file
	do {
		cout << "Enter LISP file (All those parenthesis...): ";
		cin >> fileName;
		file.open(fileName.c_str());
	} while (!file.is_open());


	binTreeNode<int> *root;
	myStack<int> path, reversePath;
	int targetSum;
	char ch;
	while (!file.eof()) {
		root = new binTreeNode<int>;
		root->item = 0;
		root->left = root->right = nullptr;

		// get target sum
		file >> targetSum;

		// continue to first '('
		file >> ch;
		if (ch == '(') {
			// get tree
			readLISP(root, file);

			// search for a path in tree
			if (evaluate(root, 0, targetSum, reversePath)) {
				// path found
				cout << "\nPath in tree exists" << endl;

				// reverse the path
				while (!reversePath.isEmpty()) {
					path.push(reversePath.top());
					reversePath.pop();
				}

				// print the path
				while (!path.isEmpty()) {
					cout << path.top();
					path.pop();
					if (path.isEmpty()) cout << " = ";
					else cout << " + ";
				}
				cout << targetSum << endl;
			} else {
				cout << "\nNo such path exists, LISP is a pain anyway" << endl;
			}

			destroyTree(root);
		}

	}


	return 0;
}

