#ifndef LINKEDLIST
#define LINKEDLIST

#include <iterator>

/*
LL: linked-list class
constructors:
	LL();
	LL(const LL &);
public functions:
	const LL &operator=(const LL &);
	iterator begin() const;
	iterator end() const;
	void headRemove();
	void tailRemove();
	void removeNode(const iterator &);
	bool isEmpty() const;
	void headInsert(const Type &);
	void tailInsert(const Type &);
	void update(const iterator &, const Type &);
*/
template <class Type>
class LL {
	struct node {
		Type item;
		node *next;
		node *prev;
	};

	public:
		/*
		iterator: classed based on built-in iterator
		constructors:
			iterator();
			iterator(node *);
		public functions:
			Type operator*();
			iterator operator++(int);
			iterator operator--(int);
			bool operator==(const iterator &) const;
			bool operator!=(const iterator &) const;
		*/
		class iterator {
			public:
				friend class LL;
				iterator();
				iterator(node *);
				Type operator*();
				iterator operator++(int);
				iterator operator--(int);
				bool operator==(const iterator &) const;
				bool operator!=(const iterator &) const;

			private:
				node *current;
		};

		LL();
		LL(const LL &);
		const LL &operator=(const LL &);
		~LL();
		iterator begin() const;
		iterator end() const;
		void headRemove();
		void tailRemove();
		void removeNode(const iterator &);
		bool isEmpty() const;
		void headInsert(const Type &);
		void tailInsert(const Type &);
		void update(const iterator &, const Type &);

	private:
		node *head;
		node *tail;
};


/*
LL<Type>::iterator::iterator: constuctor for iterator class
parameters: null
return value: null
*/
template <class Type>
LL<Type>::iterator::iterator() {
	current = nullptr;
}

/*
LL<Type>::iterator::iterator: constructor for assigning current with a pointer
parameters:
	*pointer: pointer to a node
return value: null
*/
template <class Type>
LL<Type>::iterator::iterator(node *pointer) {
	current = pointer;
}

/*
LL<Type>::iterator::operator* : returns the item field of the node that current points to
parameters: null
return value: a value of template Type of current pointer
*/
template <class Type>
Type LL<Type>::iterator::operator*() {
	return current->item;
}

/*
LL<Type>::iterator::operator++(int) : moves the iterator object
	over to the next node in the linked list
parameters:
	int : right hand side of ++ operator
return value:
	typename LL<Type>::iterator : left hand side of ++ operator
*/
template <class Type>
typename LL<Type>::iterator LL<Type>::iterator::operator++(int) {
	current = current->next;
	return current;
}

/*
LL<Type>::iterator::operator-- : moves the iterator object
	over to the previous node in the linked list
parameters:
	int : right hand side of -- operator
return value:
	typename LL<Type>::iterator : left hand side of -- operator
*/
template <class Type>
typename LL<Type>::iterator LL<Type>::iterator::operator--(int) {
	current = current->prev;
	return current;
}

/*
LL<Type>::iterator::operator== : returns true if *this iterator points
	to the same node as the rhs object, returns false otherwise
parameters:
	&rhs : right hand side of the == operator
return value:
	bool : true if same, false otherwise
*/
template <class Type>
bool LL<Type>::iterator::operator==(const iterator &rhs) const {
	return (this->current == rhs.current);
}

/*
LL<Type>::iterator::operator!= : returns false if *this iterator
	points to the same node as the rhs object, returns true otherwise
parameters:
	&rhs : right hand side of the != operator
return value:
	bool : false if same, true otherwise
*/
template <class Type>
bool LL<Type>::iterator::operator!=(const iterator &rhs) const {
	return (this->current != rhs.current);
}

/*
LL<Type>::LL : default constructor that assigns NULL to both head and tail
parameters: null
return value: null
*/
template <class Type>
LL<Type>::LL() {
	head = nullptr;
	tail = nullptr;
}

/*
LL<Type>::LL : copy constructor, performs a deep copy of the copy object to the
	this* object
parameters:
	&copy : object to copy
return value: null
*/
template <class Type>
LL<Type>::LL(const LL &copy) {
	if (copy.head == nullptr)
		return;
	
	node *temp = copy.head;
	head = new node;
	head->item = temp->item;
	head->next = nullptr;
	head->prev = nullptr;
	
	node *current = head;
	temp = temp->next;

	while (temp != nullptr) {
		current->next = new node;
		current->next->prev = current;
		current = current->next;
		current->item = temp->item;
		current->next = nullptr;
		temp = temp->next;
	}
}

/*
LL<Type>::operator= : assignment operator, performs a deep
	copy of the rhs object into the *this object
parameters:
	&rhs : right hand side of = operator
return value:
	LL<Type>& : left hand side of = operator
*/
template <class Type>
const LL<Type>& LL<Type>::operator=(const LL& rhs) {
	if (rhs.head == nullptr)
		return this;

	node *temp = rhs.head;
	this->head = new node;
	this->head->item = temp->item;
	this->head->next = nullptr;
	this->head->prev = nullptr;

	node *current = this->head;
	temp = temp->next;

	while (temp != nullptr) {
		current->next = new node;
		current->next->prev = current;
		current = current->next;
		current->item = temp->item;
		current->next = nullptr;
		temp = temp->next;
	}
}

/*
LL<Type>::~LL() : deconstructor, deallocates the linked list
parameters: null
return value: null
*/
template <class Type>
LL<Type>::~LL() {
	node *current = head;
	while (current->next != nullptr) {
		current = current->next;
		delete current->prev;
	}
	delete current;
}

/*
LL<Type>::begin : returns an iterator object whose current pointer
	is assigned with the address in the head pointer
parameter: null
return value:
	typename LL<Type>::iterator
*/
template <class Type>
typename LL<Type>::iterator LL<Type>::begin() const {
	iterator it = head;
	return it;
}

/*
LL<Type>::end : returns an iterator object whose current pointer
	is assigned with the address in the tail pointer
parameter: null
return value:
	typename LL<Type>::iterator
*/
template <class Type>
typename LL<Type>::iterator LL<Type>::end() const {
	iterator it = tail;
	return it;
}

/*
LL<Type>::removeNode : removes the node from the linked list that the
	iterator that is passed into the function is pointing to
parameter:
	&it : iterator for the node to remove
return value: void
*/
template <class Type>
void LL<Type>::removeNode(const iterator &it) {
	node *cur = head;
	while (cur != *it)
		cur = cur->next;
	node *left = cur->prev;
	left->next = cur->next;
	cur = cur->next;
	cur->prev = left;
	delete cur;
}

/*
LL<Type>::headRemove : removes the front node
parameters: null
return value: void
*/
template <class Type>
void LL<Type>::headRemove() {
	node *cur = head;
	head = cur->next;
	delete cur;
}

/*
LL<Type>::tailRemove : removes the end node
parameters: null
return value: void
*/
template <class Type>
void LL<Type>::tailRemove() {
	node *cur = tail;
	tail = cur->prev;
	delete cur;
}

/*
LL<Type>::isEmpty : returns true if the list is empty and returns false if the list is
	not empty
parameters: null
return value:
	bool : true if empty, false otherwise
*/
template <class Type>
bool LL<Type>::isEmpty() const {
	return (head == nullptr) ? true : false;
}

/*
LL<Type>::headInsert : inserts a new node to the front of the linked list
parameter:
	&element : element to insert
return value:
	void
*/
template <class Type>
void LL<Type>::headInsert(const Type &element) {
	node *newNode = new node;
	newNode->item = element;
	newNode->prev = nullptr;
	newNode->next = nullptr;
	if (head == nullptr && tail == nullptr) { // if list is empty
		head = newNode;
		tail = newNode;
	} else {
		head->prev = newNode;
		newNode->next = head;
		head = newNode;
	}
	
}

/*
LL<Type>::tailInsert : inserts a new node to the end of the linked list
parameter:
	&element : element to insert
return value:
	void
*/
template <class Type>
void LL<Type>::tailInsert(const Type &element) {
	node *newNode = new node;
	newNode->item = element;
	newNode->next = nullptr;
	newNode->prev = nullptr;
	if (head == nullptr && tail == nullptr) { // if list is empty
		head = newNode;
		tail = newNode;
	} else {
		tail->next = newNode;
		newNode->prev = tail;
		tail = newNode;
	}
}

/*
LL<Type>::update : assigns the item field of the node that the iterator object points to with the element passed into the funciton
parameters:
	&it : iterator to update
	&element : item to update to
return value:
	void
*/
template <class Type>
void LL<Type>::update(const LL<Type>::iterator &it, const Type &element) {
	node *cur = head;
	Type check = it.current->item;
	while (cur->item != check) {
		cur = cur->next;
	}
	cur->item = element;
}

#endif