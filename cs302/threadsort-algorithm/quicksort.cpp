// Quick sort in c++

#include <iostream>
#include <random>
#include <vector>

using namespace std;

vector<int> _list;

void swap(int *a, int *b) {
	int t = *a;
	*a = *b;
	*b = t;
}

int partition(int low, int high) {
	int pivot = _list[high];
	int i = (low - 1);

	for (int j = low; j < high; j++) {
		if (_list[j] <= pivot) {
			i++;
			swap(&_list[i], &_list[j]);
		}
	}

	swap(&_list[i+1], &_list[high]);
	return (i+1);
}

void quickSort(int low, int high) {
	if (low < high) {
		int pi = partition(low, high);

		quickSort(low, pi - 1);

		quickSort(pi + 1, high);
	}
}

int main() {
	int listSize;
	cout << "how big: ";
	cin >> listSize;

	for (int i = 0; i < listSize; i++) {
		int num = rand() % listSize + 1;
		_list.push_back(num);
	}

	quickSort(0, listSize-1);

	for (int i = 0; i < listSize; i++)
		cout << _list[i] << " ";
}
