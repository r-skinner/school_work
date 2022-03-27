#include <chrono>
#include <iostream>
#include <mutex>
#include <random>
#include <utility>
#include <vector>
#include <thread>

using namespace std;

constexpr long long size= 21;
mutex myMutex;

void sumUp(double& sum, const vector<double>& val, 
   unsigned int beg, unsigned int end){
   double localSum = 0;
    for (auto it= beg; it <= end; it++){
        localSum+= val[it];
    }
    lock_guard<mutex> myLock(myMutex);
    sum += localSum;
}

int main(){

  cout << endl;

  vector<double> randValues;
  randValues.reserve(size);

  randValues[0] = 1;
  for ( double i=1 ; i< size ; ++i)
     randValues[i] = randValues[i-1]/i;

  double sum = 0;
  auto start = chrono::system_clock::now();

  int threads = 7;
  thread t[threads];
  long long slice = size / threads;
  int startIdx=0;
  for (int i = 0; i < threads; ++i) {
    cout << "Thread[" << i << "] - slice ["
         << startIdx << ":" << startIdx+slice-1 << "]" << endl;
    t[i] = thread(sumUp, ref(sum), ref(randValues), startIdx, startIdx+slice-1);
    startIdx += slice;
  }

  for (int i = 0; i < threads; ++i)
     t[i].join();

  chrono::duration<double> dur= chrono::system_clock::now() - start;
  cout << "Time for addition " << dur.count() << " seconds" << endl;
  cout << "Result: " << sum << endl;

  cout << endl;

}

