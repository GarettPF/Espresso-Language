#include <fstream>
#include <cstdlib>
using namespace std;

template <class type>
struct binTreeNode {
	type item; //stores the data in the node of the tree
	binTreeNode<type> *left;
	binTreeNode<type> *right;
};

template <class type>
void readLISP(binTreeNode<type> *, ifstream &);

binTreeNode<int> *createNode(ifstream &);

template <class type>
bool evaluate(binTreeNode<type> *, int, int, myStack<type> &);

template <class type>
void destroyTree(binTreeNode<type> *);

////////////////////////////////////////////////////////////////////

/**
 * @brief : reads from a file and builds a tree (preorder)
 * @tparam type : value type of the tree
 * @param r : pointing to some node in the tree
 * @param infile : the file to read from
*/
template <class type>
void readLISP(binTreeNode<type> *r, ifstream &infile) {
	char ch;
	string buffer = "";


	// base case
	if (r == nullptr) return;

	if (r->item == 0) { // for the root
		do {
			infile >> ch;
			if (ch - '0' < 10 && ch - '0' >= 0) {
				buffer += ch;
			}
		} while (ch != '(' && ch != ')');

		infile.putback(ch);
		r->item = atoi(buffer.c_str());
	}

	// setup left node
	infile >> ch;
	if (ch == '(') {
		// next ch will be number or ')'
		infile >> ch;
		if (ch == ')') {
			r->left = nullptr;
		} else {
			infile.putback(ch);
			r->left = new binTreeNode<int>;
			infile >> r->left->item;
		}
	}
	readLISP(r->left, infile);

	// setup right node
	infile >> ch;
	if (ch == '(') {
		// next ch will be number or ')'
		infile >> ch;
		if (ch == ')') {
			r->right = nullptr;
		} else {
			infile.putback(ch);
			r->right = new binTreeNode<int>;
			infile >> r->right->item;
		}
	}
	readLISP(r->right, infile);

	// continue to next ')' for closing the current node
	do {
		infile >> ch;
	} while (ch != ')');
}

/**
 * @brief : a preorder traversal to determine if there is a 
 *			path from the root to a leaf whose numbers
 *			equal a target sum.
 * @tparam type : value type of the tree
 * @param r : pointing to some node in the tree
 * @param runningSum : current sum from root to leaf
 * @param targetSum : the sum we are checking for
 * @param path : the call stack of numbers of each leaf to root 
 * @return : true if path found, false otherwise
*/
template <class type>
bool evaluate(binTreeNode<type> *r, int runningSum, int targetSum, myStack<type> &path) {
	bool left_node, right_node;

	if (r != nullptr) {
		// push r->item to stack
		path.push(r->item);

		// base case
		if (runningSum + r->item == targetSum) {
			return true;
		}

		// when running sum > target sum
		if (runningSum + r->item > targetSum) {
			path.pop();
			return false;
		} else {
			// this means running sum + item < target sum so continue
			runningSum += r->item;
		}

		left_node = evaluate(r->left, runningSum, targetSum, path);
		right_node = evaluate(r->right, runningSum, targetSum, path);

		// if a path was found from either children
		if (left_node || right_node) {
			return true;
		} else {
			path.pop();
			return false;
		}

	} else {
		// r was nullptr
		return false;
	}
}

template <class type>
void destroyTree(binTreeNode<type> *r) {
	if (r != nullptr) {
		destroyTree(r->left);
		destroyTree(r->right);
		delete(r);
	}
}
