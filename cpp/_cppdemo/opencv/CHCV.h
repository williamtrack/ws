#pragma once
#include "opencv2/opencv.hpp"

class CHCV
{
public:
	CHCV(cv::Mat& src,std::string windows);
	CHCV();
	cv::Mat src;
	std::string winName;
	void f_paint();
	void f_change(); //change img model, save img file
	void f_mat(); //Mat using
	int f_camera(); //read camera data
	int f_histogram(); //单通道直方图
	int f_histogram3(); //3通道直方图
	int f_count(); //pixel count
	int f_koutu(); //抠图
	int f_work();
};

