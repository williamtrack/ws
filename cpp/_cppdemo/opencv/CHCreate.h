#pragma once
#include "FrameCache.h"
#include <deque>

class CHCreate
{
	std::thread m_thread;
public:
	void run(std::atomic_bool& runFlag,std::deque<FrameCache>& frameCache, std::mutex& mtx,std::condition_variable& cvar);
	void detach() { m_thread.detach(); }
	void join() { m_thread.join(); }
	~CHCreate() {
		std::cout << "delete CHCreate ~\n";
	}
};

