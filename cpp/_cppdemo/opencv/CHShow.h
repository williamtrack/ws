#pragma once
#include "FrameCache.h"
#include <deque>

class CHShow
{
	std::thread m_thread;
public:
	void run(std::atomic_bool& runFlag, std::deque<FrameCache>& frameCaches, std::mutex& mtx, std::condition_variable& cvar);
	void detach() { m_thread.detach(); }
	void join() { m_thread.join(); }

	~CHShow() {
		std::cout << "delete CHShow ~\n";
	}
};

