/**********************
*
*   Name: Garett Pascual-Folster, 2001691416, Assignment 4
*   Description: Use a set of threads to sort a given array
*   Input: 2 ints (listSize: size of the list; numThreads: number of threads;) Assuming each
*			integer input is a power of 2
*   Output: A sorted list
*
**********************/

#include <iostream>
#include <thread>
#include <string>
#include <vector>
#include <random>
#include <math.h>

using namespace std;

vector<int> _list;

/**
 * @brief Performs the insertion sort algorithm on the global
 *			variable _list.
 * 
 * @param left	Left index of _list
 * @param right Right index of _list
*/
void insertionSort(int left, int right);

/**
 * @brief Performs the merge sort of two sublists of the global
 *			variable _list.
 * 
 * @param leftLeft		left index of the left sublist
 * @param leftRight		right index of the left sublist
 * @param rightLeft		left index of the right sublist
 * @param rightRight	right index of the right sublist
*/
void mergeLists(int leftLeft, int leftRight, int rightLeft, int rightRight);

int main() {
	// get inputs for listSize and numThreads
	int listSize, numThreads;
	string input;

	do { // get listSize
		cout << "list size = ";
		cin >> input;
		try {
			listSize = stoi(input);
		} catch (exception &error) {
			cout << "not a number" << endl;
			listSize = -1;
		}
	} while (listSize < 0);

	int maxThreads = thread::hardware_concurrency();
	do { // get numThreads
		cout << "(Max Threads = " << maxThreads << ") Number of threads = ";
		cin >> input;
		try {
			numThreads = stoi(input);
		} catch (exception &error) {
			cout << "not a number" << endl;
			numThreads = -1;
		}
	} while (numThreads <= 0 || numThreads > maxThreads);



	// initialize the vector _list
	srand((unsigned)time(NULL)); // randomize seed
	for (int i = 0; i < listSize; i++) {
		int num = rand() % listSize + 1;
		_list.push_back(num);
		cout << num << " ";
	}
	cout << "\n\nSorting...\n\n";


	// create threads on each sublist using insertionSort function
	vector<thread> threadPool;
	int size = listSize / numThreads, left;
	for (left = 0; left <= listSize - size; left += size)
		threadPool.push_back(thread(insertionSort, left, left+size-1));
	
	// clear threadPool
	for (unsigned int i = 0; i < threadPool.size(); i++)
		threadPool[i].join();
	threadPool.clear();



	// merge adjacent pairs of sublists using mergeLists function and continue merging until finished
	int newSize, halfSize;
	for (int iter = 2; iter <= numThreads; iter *= 2) {
		// set up the size for the sublists
		newSize = size * iter;
		halfSize = newSize / 2;

		// create and push threads to threadPool
		for (left = 0; left <= listSize - newSize; left += newSize)
			threadPool.push_back(thread(
				mergeLists, left, left + halfSize - 1, left + halfSize, left + newSize - 1
			));

		// clear threadPool
		for (unsigned int i = 0; i < threadPool.size(); i++)
			threadPool[i].join();
		threadPool.clear();
	}



	// print results
	for (int i = 0; i < listSize; i++)
		cout << _list[i] << " ";
	cout << endl;

	return 0;
}


void insertionSort(int left, int right) {
	int i, key, j;

	for (i = left; i <= right; i++) {

		key = _list[i];
		j = i - 1;

		// move ints that are > j one position forward
		while (j >= left && _list[j] > key) {
			_list[j + 1] = _list[j];
			j = j - 1;
		}
		_list[j + 1] = key;

	}
}

void mergeLists(int leftLeft, int leftRight, int rightLeft, int rightRight) {
	vector<int> sortedList;
	int beg = leftLeft, end = rightRight;

	// compare the two sublist leftmost values until one sublist is done
	while (leftLeft <= leftRight && rightLeft <= rightRight) {
		if (_list[leftLeft] < _list[rightLeft]) {
			sortedList.push_back(_list[leftLeft]);
			leftLeft++;
		}
		else {
			sortedList.push_back(_list[rightLeft]);
			rightLeft++;
		}
	}

	// insert the rest of the remaining sublists if possible
	while (leftLeft <= leftRight) {
		sortedList.push_back(_list[leftLeft]);
		leftLeft++;
	}
	while (rightLeft <= rightRight) {
		sortedList.push_back(_list[rightLeft]);
		rightLeft++;
	}

	// replace the new sorted list into _list
	for (int i = 0; beg <= end; i++) {
		_list[beg] = sortedList[i];
		beg++;
	}
}