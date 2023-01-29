#include <thread>
#include <iostream>

using namespace std;

int main() {
	int cores = thread::hardware_concurrency();
	cout << cores << " Threads are supported" << endl;	
	return 0;
}
