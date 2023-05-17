#pragma once

#include <opencv2/opencv.hpp>

extern "C" {
#include <libavcodec/avcodec.h>
#include <libswscale/swscale.h>
#include <libavformat/avformat.h>
};

class CVHelper
{
public:
	CVHelper();
	void show(const AVFrame* frame);
private:
	cv::Mat avFrame2cvMat(const AVFrame* frame);
	std::string winName = "ffmpeg";
};

