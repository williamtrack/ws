#include "CHCreate.h"
#include "CHtime.h"

void CHCreate::run(std::atomic_bool& runFlag, std::deque<FrameCache>& frameCaches, std::mutex& mtx, std::condition_variable& cvar)
{
	m_thread = std::thread([&]() {
		CHTime chtime;
		for (int iter = 0; runFlag; iter++) {
			std::this_thread::sleep_for(std::chrono::milliseconds(34));
			std::unique_lock<std::mutex> lock(mtx);
			FrameCache frameCache;
			cv::Mat img(cv::Size(480, 360), CV_8UC3, cv::Scalar(255, 0, 0));
			for (int view = 0; view < 1; view++) {
				//img.setTo(cv::Scalar(255, 0, 0));
				cv::putText(img, chtime.gt(), cv::Point(100, 100), cv::FONT_HERSHEY_DUPLEX, 2.0, cv::Scalar(0, 0, 255), 2);
				frameCache.view = view;
				//frameCache.imgs[view] = img; //未push不能直接赋值
				frameCache.imgs.push_back(img);
				std::cout << "create ===\n";
			}
			frameCaches.emplace_back(frameCache);
			lock.unlock();
			cvar.notify_one();
		}
		std::cout << "create loop end.\n";
		}
	);
}
