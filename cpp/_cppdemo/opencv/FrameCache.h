#pragma once
#include <opencv2/opencv.hpp>
struct FrameCache
{
	std::vector<cv::Mat> imgs;
	size_t view = 0;
};