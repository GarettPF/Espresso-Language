template <class Type>
class myStack {
	public:
		myStack();
		myStack(const myStack<Type> &);
		const myStack<Type>& operator=(const myStack<Type> &);
		~myStack();

		void push(const Type &);
		void pop();
		Type top() const;
		bool isEmpty() const;

	private:
		struct node {
			Type item;
			node *next;
		};

		node *topOfmyStack;
};

/**
 * @brief : Default constructor
 * @tparam Type : value type stored in stack
*/
template <class Type>
myStack<Type>::myStack() {
	this->topOfmyStack = nullptr;
}

/**
 * @brief : copy constructor
 * @tparam Type : value type stored in stack
 * @param copy : stack obj to deep copy
*/
template <class Type>
myStack<Type>::myStack(const myStack<Type> &copy) {
	this->topOfmyStack = nullptr;

	// iterate through copy's stack
	for (node *it_node = copy.topOfmyStack; it_node != nullptr; it_node = it_node->next) {
		if (this->topOfmyStack == nullptr) {
			node *head = new node;
			head->item = it_node->item;
			head->next = it_node->next;

			// Assign node to topOfmyStack
			this->topOfmyStack = head;
		} else {
			this->push(it_node->item);
		}
	}
}

/**
 * @brief : assignment operator for deep copy
 * @tparam Type : value type stored in stack
 * @param rhs : stack obj to deep copy
 * @return : the lhs of the "=" operator
*/
template<class Type>
const myStack<Type>& myStack<Type>::operator=(const myStack<Type> &rhs) {
	this->topOfmyStack = nullptr;

	// iterate through rhs's stack
	for (node *it_node = rhs.topOfmyStack; it_node != nullptr; it_node = it_node->next) {
		if (this->topOfmyStack == nullptr) {
			node *head = new node;
			head->item = it_node->item;
			head->next = it_node->next;

			// Assign node to topOfmyStack
			this->topOfmyStack = head;
		} else {
			this->push(it_node->item);
		}
	}

	return *this;
}

/**
 * @brief : deconstructor
 * @tparam Type : value type stored in stack
*/
template<class Type>
myStack<Type>::~myStack() {
	// iterate through stack and delete
	while (!isEmpty())
		pop();
}

/**
 * @brief : head insert or push value
 * @tparam Type : value type stored in stack
 * @param insert : the value to push onto the stack
*/
template <class Type>
void myStack<Type>::push(const Type &insert) {
	node *newNode = new node;
	newNode->item = insert;
	newNode->next = topOfmyStack;

	topOfmyStack = newNode;
}

/**
 * @brief : head remove or pop value
 * @tparam Type : value type stored in stack
*/
template <class Type>
void myStack<Type>::pop() {
	node *top = topOfmyStack;
	topOfmyStack = topOfmyStack->next;

	delete top;
}

/**
 * @brief : gets the value of the top node
 * @tparam Type : value type stored in stack
 * @return : the value of the top node
*/
template <class Type>
Type myStack<Type>::top() const {
	return topOfmyStack->item;
}

/**
 * @brief : checks if stack is empty
 * @tparam Type : value type stored in stack
 * @return : true if empty, false otherwise
*/
template <class Type>
bool myStack<Type>::isEmpty() const {
	return topOfmyStack == nullptr;
}