#include "CHCV.h"
#include "arithmetic.h"

using namespace cv;
CHCV::CHCV(cv::Mat& src, std::string windows)
{
	this->src = src;
	this->winName = windows;
}

CHCV::CHCV():
	winName("win"),
	src(cv::Mat())
{}

void CHCV::f_change()
{
	Mat gray, hsv;
	cvtColor(src, hsv, COLOR_BGR2HSV);
	cvtColor(src, gray, COLOR_BGR2GRAY);
	imshow("HSV", hsv);
	imshow("灰度", gray);
	//imwrite("./hsv.png", hsv);
	//imwrite("./gray.png", gray);
}

void CHCV::f_paint()
{
	Point start = Point(10, 100);
	Point end = Point(20, 200);
	line(src, start, end, Scalar(0, 0, 255));
	rectangle(src, Point(0, 0), Point(50, 25), Scalar(0, 0, 255), 5, LINE_4, 0); //左上->右下
	imshow("", src);
}

void CHCV::f_mat()
{
	//=====创建=====
	//默认构造函数与值构造函数
	cv::Mat img; //默认构造函数

	//创建自定义矩阵
	cv::Mat dist = (cv::Mat_<float>(5, 1) << 0.99,0.2,0.3,0.4,0.5);
	//cv::Mat dist = cv::Mat(5,1, CV_32FC1, cv::Scalar(0.999));
	printf("==%f\n", dist.at<float>(0, 0));

	//值构造函数1
	cv::Mat img1(480, 640, CV_8UC3); //图像高（行）480-row，宽（列）640-col， 数据类型：CV_8UC3

	//值构造函数2
	//cv::Scalar(B, G, R)可以表示三通道颜色，这里所示为纯蓝色
	cv::Mat img2(480, 640, CV_8UC3, cv::Scalar(255, 0, 0));

	//值构造函数3
	//效果与上面一样
	cv::Size sz3(480, 640);
	cv::Mat img3(sz3, CV_8UC3, cv::Scalar(255, 0, 0));

	//拷贝构造函数1---都是以静态引用传递参数（const &）
	cv::Mat img4(img3);

	//拷贝构造函数2---只拷贝感兴趣的区域----由Rect对象定义
	//rect左上角（100，100），宽高均为200，（x,y,width,height)
	cv::Rect rect(100, 100, 200, 200);
	cv::Mat img5(img3, rect);

	//拷贝构造函数3--从指定行列构造
	//从img3中拷贝0-239行以及0-319行到img6
	cv::Range rows(0, 240);
	cv::Range cols(0, 320);
	cv::Mat img6(img3, rows, cols);

	//静态构造函数
	cv::Mat img7 = cv::Mat::zeros(480, 640, CV_8UC3); //480行640列，值全为零的数组。
	cv::Mat img8 = cv::Mat::ones(480, 640, CV_64FC1); //全1矩阵
	cv::Mat img9 = cv::Mat::eye(480, 640, CV_16SC2); //单位矩阵

	//赋值
	img9.setTo(Scalar(255, 0, 0));

	//=====访问元素======
	//使用示例

	//直接访问---模板函数at<>()
	//单通道, 尖括号里面的类型照着文章开头介绍的类型对应关系输入
	cv::Mat img10 = cv::Mat::ones(100, 100, CV_32FC1);
	img10.at<float>(10, 10) = 100.0;
	float elemf = img10.at<float>(10, 10); //赋值
	printf("%f\n", elemf);

	//多通道---Vec3b代表固定向量类
	//注意类型之间的对应，固定向量与Mat的代表字母有一点差异
	cv::Mat img11(500, 500, CV_8UC3, cv::Scalar(255, 255, 0));
	Vec3b elem = img11.at<Vec3b>(10, 10);
	uchar elem_B = elem[0]; //蓝色通道数值---255

	Vec3b temp;
	for (int i = 0; i < img11.rows; ++i)
	{
		for (int j = 0; j < img11.cols; ++j)
		{
			temp = rainbow(img11.cols, j);
			//temp[0] = rand() % 255;
			//temp[1] = rand() % 255;
			//temp[2] = rand() % 255;
			img11.at<Vec3b>(i, j) = temp;
		}
	}
	imshow(winName, img11);

	//=======内置函数=====
	//克隆矩阵
	cv::Mat img12(480, 640, CV_64FC3);
	cv::Mat img13 = img.clone();

	//设置元素值
	img13.setTo(cv::Scalar(1.0, 2.0, 3.0));

	//返回通道数目
	size_t num_c = img13.channels();

	//返回数组大小
	cv::Size sz = img13.size();

	//检验数组是否为空,为空返回true
	bool e = img13.empty();

}

int CHCV::f_camera()
{
	VideoCapture capture;
	capture.open(0);  //读入默认摄像头
	//VideoCapture cap(0);
	if (!capture.isOpened()) {
		std::cout << "video not opeSn.\n";
		return -1;
	}

	Mat frame;
	while (true)
	{
		capture >> frame;
		if (frame.empty()) {
			std::cout << "get frame error.\n";
			return -1;
		}
		imshow("调用摄像头", frame);
		waitKey(30); //30ms
	}
	return 0;

	////遍历该图像每一个像素，并输出 BGR 值
	//for (int i = 0; i < frame.rows; i++) {
	//	for (int j = 0; j < frame.cols; j++) {
	//		Vec3b bgr = frame.at<Vec3b>(i, j);
	//		printf("b = %d, g = %d, r = %d\n", bgr[0], bgr[1], bgr[2]);
	//	}
	//}

}

int CHCV::f_histogram()
{
	Mat gray;
	cvtColor(src, gray, COLOR_BGR2GRAY);
	//设置提取直方图的相关变量
	Mat hist;  //用于存放直方图计算结果
	const int channels[1] = { 0 };  //通道索引
	float inRanges[2] = { 0,255 };
	const float* ranges[1] = { inRanges };  //像素灰度值范围
	const int bins[1] = { 256 };  //直方图的维度，其实就是像素灰度值的最大值
	calcHist(&gray, 1, channels, Mat(), hist, 1, bins, ranges);  //计算图像直方图

	//准备绘制直方图
	int width = 2;
	int hist_w = 512;
	int hist_h = 500;
	Mat histImage = Mat::zeros(hist_h, hist_w, CV_8UC3);
	normalize(hist, hist, 0, histImage.rows, NORM_MINMAX, -1, Mat()); //归一化

	printf("%d~%d\n", hist.cols, hist.rows);
	for (int i = 0; i <= hist.rows; i++)
	{
		//printf("%d\n", cvRound(hist.at<float>(i - 1)));
		rectangle(histImage, Point(width * i, hist_h - 1),
			Point(width * i + 1, hist_h - cvRound(hist.at<float>(i))),
			Scalar(255, 255, 255), -1);
	}
	imshow(winName, histImage);
	//imshow("gray", gray);
	return 0;
}

int CHCV::f_histogram3()
{
	Mat dst;
	if (src.empty())
	{
		std::cout << "Did not find the image" << std::endl;
		return -1;
	}

	/// 分割成3个单通道图像 ( R, G 和 B )
	std::vector<Mat> rgb_planes;
	split(src, rgb_planes);

	/// 设定bin数目
	int histSize = 256;

	/// 设定取值范围 ( R,G,B) )
	float range[] = { 0, 255 };
	const float* histRange = { range };

	bool uniform = true; bool accumulate = false;

	Mat b_hist, g_hist, r_hist;

	/// 计算直方图:
	calcHist(&rgb_planes[0], 1, 0, Mat(), b_hist, 1, &histSize, &histRange, uniform, accumulate);
	calcHist(&rgb_planes[1], 1, 0, Mat(), g_hist, 1, &histSize, &histRange, uniform, accumulate);
	calcHist(&rgb_planes[2], 1, 0, Mat(), r_hist, 1, &histSize, &histRange, uniform, accumulate);

	// 创建直方图画布
	int hist_w = 600; int hist_h = 400;
	int bin_w = cvRound((double)hist_w / histSize);

	Mat histImage(hist_w, hist_h, CV_8UC3, Scalar(0, 0, 0));

	/// 将直方图归一化到范围 [ 0, histImage.rows ]
	normalize(b_hist, b_hist, 0, histImage.rows, NORM_MINMAX, -1, Mat());
	normalize(g_hist, g_hist, 0, histImage.rows, NORM_MINMAX, -1, Mat());
	normalize(r_hist, r_hist, 0, histImage.rows, NORM_MINMAX, -1, Mat());

	/// 在直方图画布上画出直方图
	for (int i = 1; i < histSize; i++)
	{
		line(histImage, Point(bin_w * (i - 1), hist_h - cvRound(b_hist.at<float>(i - 1))),
			Point(bin_w * (i), hist_h - cvRound(b_hist.at<float>(i))),
			Scalar(255, 0, 0), 2, 8, 0);
		line(histImage, Point(bin_w * (i - 1), hist_h - cvRound(g_hist.at<float>(i - 1))),
			Point(bin_w * (i), hist_h - cvRound(g_hist.at<float>(i))),
			Scalar(0, 255, 0), 2, 8, 0);
		line(histImage, Point(bin_w * (i - 1), hist_h - cvRound(r_hist.at<float>(i - 1))),
			Point(bin_w * (i), hist_h - cvRound(r_hist.at<float>(i))),
			Scalar(0, 0, 255), 2, 8, 0);
	}

	/// 显示直方图
	namedWindow("calcHist Demo", WINDOW_FREERATIO);
	imshow("calcHist Demo", histImage);
	return 0;
}

int CHCV::f_count()
{
	printf("%d~%d\n", src.cols, src.rows);

	Mat gray;
	cvtColor(src, gray, COLOR_BGR2GRAY);
	resize(gray, gray, Size(500, 500)); //变更分辨率
	imshow(winName, gray);
	printf("type: %d\n", gray.type()); //mat类型
	printf("%d~%d\n", gray.cols, gray.rows);

	clock_t start = clock();      //获取当前系统时间
	//统计
	int num = 0;
	for (int i = 0; i < gray.rows; i++) {
		for (int j = 0; j < gray.cols; j++) {
			if (0 <= gray.at<uchar>(i, j) && gray.at<uchar>(i, j) <= 10)num++;
		}
	}
	printf("num: %d\n", num);


	//mask
	Mat mask;
	//将规定区间内的值置为255；必须使用Scalar()来赋值
	inRange(gray, Scalar(0, 0, 0), Scalar(10, 0, 0), mask); //mask为二值图 0/255，CV_8UC
	printf("num: %f\n", sum(mask)[0] / 255/ gray.rows/ gray.cols); //求和函数
	namedWindow("mask", WINDOW_AUTOSIZE);
	imshow("mask", mask);

	//最大最小值
	double minVal; double maxVal; Point minLoc; Point maxLoc;
	minMaxLoc(gray, &minVal, &maxVal, &minLoc, &maxLoc, Mat());
	printf("min: %.2f, max: %.2f \n", minVal, maxVal);
	printf("min loc: (%d, %d) \n", minLoc.x, minLoc.y);
	printf("max loc: (%d, %d)\n", maxLoc.x, maxLoc.y);

	//均值和方差
	Mat means, stddev;
	meanStdDev(src, means, stddev);
	printf("blue channel->> mean: %.2f, stddev: %.2f\n", means.at<double>(0, 0), stddev.at<double>(0, 0));
	printf("green channel->> mean: %.2f, stddev: %.2f\n", means.at<double>(1, 0), stddev.at<double>(1, 0));
	printf("red channel->> mean: %.2f, stddev: %.2f\n", means.at<double>(2, 0), stddev.at<double>(2, 0));
	clock_t end = clock();
	double programTimes = ((double)end - start) / CLOCKS_PER_SEC;
	printf("time: %f\n", programTimes);

	return 0;
}

int CHCV::f_koutu()
{
	resize(src, src, Size(400, 300));
	Mat image_hsv;
	cvtColor(src, image_hsv, COLOR_BGR2HSV);
	namedWindow("image_hsv", WINDOW_AUTOSIZE);
	moveWindow("image_hsv", 400, 200);
	imshow("image_hsv", image_hsv);
	Mat mask;
	//将规定区间内的值置为255；必须使用Scalar()来赋值
	inRange(image_hsv, Scalar(35, 43, 46), Scalar(99, 255, 255), mask);
	//mask为二值图像，CV_8UC
	namedWindow("mask", WINDOW_AUTOSIZE);
	moveWindow("mask", 400, 200);
	imshow("mask", mask);
	Mat output;
	bitwise_and(src, src, output, mask); //and操作
	namedWindow("output", WINDOW_AUTOSIZE);
	moveWindow("output", 400, 200);
	imshow("output", output);
	return 0;
}

int CHCV::f_work()
{
	Mat gray;
	cvtColor(src, gray, COLOR_BGR2GRAY);
	resize(gray, gray, Size(500, 500)); //变更分辨率
	imshow("gray", gray);
	printf("type: %d\n", gray.type()); //mat类型
	printf("%d~%d\n", gray.cols, gray.rows);

	//mask
	Mat mask;
	//将规定区间内的值置为255；必须使用Scalar()来赋值
	inRange(gray, Scalar(200, 0, 0), Scalar(256, 0, 0), mask); //mask为二值图 0/255，CV_8UC
	printf("num: %f\n", sum(mask)[0] / 255 / gray.rows / gray.cols); //求和函数
	namedWindow("mask", WINDOW_AUTOSIZE);
	imshow("mask", mask);


	//设置提取直方图的相关变量
	Mat hist;  //用于存放直方图计算结果
	const int channels[1] = { 0 };  //通道索引
	float inRanges[2] = { 0,255 };
	const float* ranges[1] = { inRanges };  //像素灰度值范围
	const int bins[1] = { 256 };  //直方图的维度，其实就是像素灰度值的最大值
	calcHist(&gray, 1, channels, Mat(), hist, 1, bins, ranges);  //计算图像直方图

	//准备绘制直方图
	int width = 2;
	int hist_w = 512;
	int hist_h = 500;
	Mat histImage = Mat::zeros(hist_h, hist_w, CV_8UC3);
	normalize(hist, hist, 0, histImage.rows, NORM_MINMAX, -1, Mat()); //归一化

	printf("%d~%d\n", hist.cols, hist.rows);
	for (int i = 0; i <= hist.rows; i++)
	{
		//printf("%d\n", cvRound(hist.at<float>(i - 1)));
		rectangle(histImage, Point(width * i, hist_h - 1),
			Point(width * i + 1, hist_h - cvRound(hist.at<float>(i))),
			Scalar(255, 255, 255), -1);
	}
	imshow("hist", histImage);

	return 0;
}
