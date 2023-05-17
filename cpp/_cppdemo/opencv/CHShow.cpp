#include "CHShow.h"
#include "CHtime.h"

void CHShow::run(std::atomic_bool& runFlag, std::deque<FrameCache>& frameCaches, std::mutex& mtx, std::condition_variable& cvar)
{
	m_thread = std::thread([&]() {
		CHTime chtime;
		for (int iter = 0; runFlag; iter++) {
			//std::cout << frameCaches.size() << std::endl;
			std::unique_lock<std::mutex> lock(mtx);
			//如果空则等待create线程唤醒cvar
			while (frameCaches.empty()) {
				if (cvar.wait_for(lock, std::chrono::seconds(5)) == std::cv_status::timeout) {
					if (!runFlag) {
						std::cout << "timeout, quit.\n";
						break;
					}
					else {
						std::cout << "timeout, continue.\n";
						continue;
					}
				}
			}

			//取出后立即出栈
			FrameCache frameCache = frameCaches.front();
			frameCaches.pop_front();
			lock.unlock();

			for (int view = 0; view < 1; view++) {
				//main线程不要去用cv::waitKey()否则会导致其他线程的cv不能执行
				cv::imshow("img", frameCache.imgs[view]);
				std::cout << "show ===\n";
			}
			cv::waitKey(1); //不能用Sleep(),否则卡死; 必须用waitKey()来等待cv::imshow绘制完成
		}
		cv::destroyAllWindows();
		std::cout << "show loop end.\n";
		}
	);
}
