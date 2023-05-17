#include "CHThread.h"

#include <future>
#include <string>

int CHThread::cnt = 20;
std::mutex CHThread::mtx;
using namespace std;

std::condition_variable condition;

std::string common_value="jkljkfenlj";


//set_value 只能被调用一次
void Thread_Fun1(std::promise<int>& p)
{
    Sleep(3000);
    std::this_thread::sleep_for(std::chrono::seconds(3)); //线程休眠3s
    int iVal = 123;
    std::cout << "传入数据(int)：" << iVal << std::endl;
    //传入数据iVal
    p.set_value(iVal); //该句说明调用时必须用join
}

void Thread_Fun2(std::future<int>& f)
{
    //阻塞函数，直到收到相关联的std::promise对象传入的数据
    auto iVal = f.get();		//iVal = 233

    std::cout << "收到数据(int)：" << iVal << std::endl;
}

void Thread_Fun4(promise<int>& p) {
    std::this_thread::sleep_for(std::chrono::seconds(2)); //线程休眠
    int iVal = 233;
    p.set_value(iVal); //该句说明调用时必须用join
}

void Thread_Fun3()
{
    promise<int> pr1;
    future<int> fu1 = pr1.get_future();
    thread t_temp(Thread_Fun4, ref(pr1));
    cout << "收到数据(int)：" << fu1.get() << endl; //在get时候需要提前设置好thread4
    t_temp.join();

    ////传入数据iVal
    //p.set_value(iVal); //该句说明调用时必须用join
}

void Thread_Fun5() {
    Sleep(1000);
    std::cout << common_value << std::endl;
    common_value = "abcdefghijklmnopqrstuvwxyz";
}

void Thread_Fun6() {
    Sleep(4000);
    std::cout << common_value << std::endl;
}



void CHThread::test()
{
    std::cout << "main pid: " << std::this_thread::get_id() << std::endl;
    //// === join&detach //阻塞并行
    //std::thread th1(t1);
    //std::thread th2(t2);
    std::thread th1(Thread_Fun5);
    std::thread th2(Thread_Fun6);
    //printf("%u\n", th1.get_id());
    //printf("%u\n", th2.get_id());
    //th1.join(); //阻塞
    //th2.join(); //阻塞
    th1.detach(); //并行
    th2.detach(); //并行

    //// === lockGuard //进程锁
    //std::thread th3(t3);
    //std::thread th4(t4);
    //th3.detach();
    //th4.detach();

    //// === promise&future //同步锁
    //std::promise<int> pr1;
    ////声明一个std::future对象fu1，并通过std::promise的get_future()函数与pr1绑定
    //std::future<int> fu1 = pr1.get_future();
    ////创建一个线程t1，将函数Thread_Fun1及对象pr1放在线程里面执行
    //std::thread t1(Thread_Fun1, std::ref(pr1));
    ////创建一个线程t2，将函数Thread_Fun2及对象fu1放在线程里面执行
    //std::thread t2(Thread_Fun2, std::ref(fu1));
    ////阻塞至线程结束
    //t1.join(); 
    //t2.detach();

    //// ===同步锁
    //thread t1(Thread_Fun3);
    //t1.detach();


    //// ===wait_for
    //waitfor();

}


void CHThread::waitfor() {
    std::atomic_bool runFlag = true;
    bool wakeFlag = false;
    std::thread([&]() {
        std::this_thread::sleep_for(std::chrono::seconds(1));
        wakeFlag = true;
        //condition.notify_one();
        condition.notify_all();
        }).detach();
        std::unique_lock<std::mutex> lck(mtx);
        while (runFlag) {
            //判断超时, 方法1
            //如果中途退出则返回true; 如果达到时间点则返回false,
            if (!condition.wait_until(lck, std::chrono::system_clock::now() += std::chrono::seconds(5), [&] {
                if (wakeFlag) {
                    std::cout << "wake\n";
                    return true;
                }
                return false;
                }
            ))
                std::cout << "out of time.\n";

                ////方法2
                ////最后一个参数是预制条件，调用wait_for的时候，
                ////首先就会判断这个条件，如果这个条件返回false，那么会继续等待
                ////如果在超时之前，收到了一个notify, 那么他会再次执行这个预制条件来进行判断
                ////超时的时候再执行这个条件，条件成立就不会再进行等待, 算上notify此处共执行3次
                //bool out = false;
                //condition.wait_for(lck, std::chrono::seconds(5), [&] {
                //	std::cout << "condition" << std::endl;
                //	if (wakeFlag) {
                //		std::cout << "wake\n";
                //		out = false;
                //		return true;
                //	}
                //	else {
                //		out = true;
                //		return false;
                //	}
                //	}
                //);
                //if (out) {
                //	std::cout << "out of time\n";
                //	break;
                //}

                ////方法3, 有概率假唤醒, 不能用于超时判定? 只能用于线程等待唤醒
                //if (condition.wait_for(lck, std::chrono::seconds(4)) == std::cv_status::timeout) {
                //	std::cout << "timeout.\n"; //有概率假唤醒,返回no_timeout
                //	break;
                //}

                wakeFlag = false;
                //runFlag = false;

        }
        std::cout << "end\n";
}

void CHThread::t1()
{
    for (int i = 0; i < 10; ++i) {
        printf("t1 tread\n");
        Sleep(100);
    }
    std::cout << std::this_thread::get_id() << std::endl;
}

void CHThread::t2()
{
    for (int i = 0; i < 10; ++i) {
        printf("t2 tread\n");
        Sleep(100);
    }
    std::cout << std::this_thread::get_id() << std::endl;
}



//lock_guard不能在中途解锁, 只能通过析构时解锁

void CHThread::t3()
{
    while (cnt > 0)
    {
        //std::lock_guard<std::mutex> lk(mtx);
        //mtx.lock();
        std::unique_lock<std::mutex> lock(mtx);
        if (cnt > 0)
        {
            //sleep(1);
            --cnt;
            printf("t3: ");
            std::cout << cnt << std::endl;
        }
        if (cnt < 12) {
            lock.unlock();
            std::cout << "unlock\n ";
        }
        //mtx.unlock();
    }
}
void CHThread::t4()
{
    while (cnt > 0)
    {
        //std::lock_guard<std::mutex> lk(mtx);
        //m.lock();
        std::unique_lock<std::mutex> lock(mtx);
        if (cnt > 3)
        {
            --cnt;
            printf("t4: ");
            std::cout << cnt << std::endl;
        }
        lock.unlock();
        //m.unlock();
    }

    //// Thread gubbins
    //std::mutex _mutex;
    //std::condition_variable _fullBuf;
    //std::condition_variable _empty;
    //std::unique_lock<std::mutex> lock(_mutex);
    //_fullBuf.wait(lock);
}
