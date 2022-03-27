#include <chrono>
#include <iostream>

void static_array(){
    static int foo[5000]{};
}

void stack_array(){
    int foo[5000]{};
}

void heap_array(){
    int* foo = new int[5000];
}

int main () {
    int iters = 1000000;
    long long total = 0;
    std::chrono::high_resolution_clock::time_point start;
    std::chrono::high_resolution_clock::time_point end;

    // static array
    for(int i = 0; i < iters; i++){
        start = std::chrono::high_resolution_clock::now();
        static_array();
        end = std::chrono::high_resolution_clock::now();
        total += std::chrono::duration_cast<std::chrono::microseconds>( end - start ).count();
    }
    std::cout << "Static avg in microseconds:\t" << double(total)/iters<< std::endl;


    // stack array
    total = 0;
    for(int i = 0; i < iters; i++){
        start = std::chrono::high_resolution_clock::now();
        stack_array();
        end = std::chrono::high_resolution_clock::now();
        total += std::chrono::duration_cast<std::chrono::microseconds>( end - start ).count();
    }
    std::cout << "Stack avg in microseconds:\t" <<  double(total)/iters << std::endl;


    // heap array
    total = 0;
    for(int i = 0; i < iters; i++){
        start = std::chrono::high_resolution_clock::now();
        heap_array();
        end = std::chrono::high_resolution_clock::now();
        total += std::chrono::duration_cast<std::chrono::microseconds>( end - start ).count();
    }
    std::cout << "Heap avg in microseconds:\t" << double(total)/iters << std::endl;


    return 0;
}
