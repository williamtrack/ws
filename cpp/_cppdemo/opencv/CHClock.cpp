#include "CHClock.h"

#include <opencv2/opencv.hpp>
#include <CHTime.h>

void CHClock::test()
{
	cv::Mat img(cv::Size(480, 360), CV_8UC3, cv::Scalar(0, 0, 255));
	cv::namedWindow("img", cv::WINDOW_KEEPRATIO);
	CHTime chtime;
	while (1)
	{
		chtime.pt();
		img.setTo(cv::Scalar(0, 0, 255));
		cv::putText(img, chtime.gt(), cv::Point(10, 100), cv::FONT_HERSHEY_DUPLEX, 2.5, cv::Scalar(255, 255, 0), 3);//Ð´×Ö
		cv::imshow("img", img);
		cv::waitKey(1);
	}
}
