#include "CVHelper.h"

cv::Mat CVHelper::avFrame2cvMat(const AVFrame* frame) {
	if (frame == nullptr)return cv::Mat();

	int width = frame->width;
	int height = frame->height;
	//    if(frame->format != AVPixelFormat::AV_PIX_FMT_BGR24){
	//        std::cout<<"frame format is not correct."<<std::endl;
	//        return cv::Mat();
	//    }

	cv::Mat result(height, width, CV_8UC3);

	//    int line_size = frame->linesize[0];
	//    std::cout<<"frame line_size: "<<line_size<<std::endl;

	SwsContext* convert_context = sws_getContext(frame->width, frame->height, static_cast<AVPixelFormat>(frame->format),
		width, height, AVPixelFormat::AV_PIX_FMT_BGR24,
		SWS_FAST_BILINEAR, nullptr, nullptr, nullptr);
	int line_size[1];
	line_size[0] = result.step1();
	//std::cout << "line_size: " << result.step1() << std::endl;

	sws_scale(convert_context, frame->data, frame->linesize, 0, height, reinterpret_cast<uint8_t* const*>(&result.data), line_size);
	sws_freeContext(convert_context);

	return result;
}

CVHelper::CVHelper()
{
	cv::namedWindow(winName, cv::WINDOW_KEEPRATIO);
	//cv::resizeWindow(winName, 800, 450);
}

void CVHelper::show(const AVFrame* frame)
{
	cv::imshow(winName, avFrame2cvMat(frame));
	cv::waitKey(1);
}
