#define _CRT_SECURE_NO_WARNINGS

#include <iostream>
using namespace std;

extern "C"
{
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include "libavdevice/avdevice.h"
}

#include "CHtime.h"

#include "decode_h264_test.h"

#include "stream_encoder.h"
#include "stream_decoder.h"
#include "video_capture.h"
#include "camera_capture.h"
#include "CVHelper.h"

int stream_encoder_test();
int stream_decoder_test();
int video_capture_test();

int main()
{
	//std::cout << "main tread: " << std::this_thread::get_id << std::endl;
	//printf("%s", avcodec_configuration());

	stream_encoder_test();
	//decode_h264_test();
	//stream_decoder_test();
	//video_capture_test();
	system("pause");
	return 0;
}

//编码avframe数据->avpacket
int stream_encoder_test() {
	int width = 640, height = 480;
	CHTime chtime;
	const char* out = "D:/data/ws/cpp/_res/video.h265";
	FILE* outfile = fopen(out, "wb+");
	int n = 0;

	//AVCodecID avcodecid= AV_CODEC_ID_H265
	AVCodecID avcodecid = AV_CODEC_ID_H264;
	stream_encoder stream_encoder;

	auto callback = [&](AVCodecContext* ctx, const AVPacket* avpkt) {
		//fwrite(avpkt->data, avpkt->size, 1, oFile);
		int flags = avpkt->flags;
		std::cout << "size " << avpkt->size << std::endl;
		printf("avpkt->flag:%d, 0x%x%x%x%x%x\n", flags & AV_PKT_FLAG_KEY, avpkt->data[0], avpkt->data[1], avpkt->data[2], avpkt->data[3], avpkt->data[4]);
		chtime.pt("end");
		fwrite(avpkt->data, 1, avpkt->size, outfile);
		fflush(outfile);
	};

	stream_encoder.load(callback, avcodecid, width, height);

	camera_capture capture;

	if (!capture.Load("video=HD USB Camera", width, height)) {
		capture.Release();
		return 0;
	}
	AVFrame* frame;
	frame = capture.NextFrame();
	//while ((frame = capture.NextFrame()) != NULL) {
	//	//chtime.pt("start");
	//	stream_encoder.encode(frame);
	//	if (n++ == 100) break;
	//}

	fclose(outfile);
	capture.Release();
}

//分段数据解码
int stream_decoder_test() {
	stream_decoder dec;

	CVHelper cvhelper;
	auto callback = [&](AVCodecContext* ctx, const AVFrame* frame) {
		//WriteYUV420ToFile(frame, ofile);
		//std::cout << std::this_thread::get_id << std::endl;
		//std::cout << frame->height << std::endl;
		cvhelper.show(frame);
		printf("decode success.\n");
	};
	dec.load(callback, AV_CODEC_ID_H264);

	const char* iFileName = "D:/data/ws/cpp/_res/test.h264";
	FILE* ifile = fopen(iFileName, "rb");
	if (ifile == NULL)
		return -1;
	uint8_t inbuf[INBUF_SIZE + AV_INPUT_BUFFER_PADDING_SIZE];
	while (!feof(ifile)) {
		/* read raw data from the input file */
		size_t data_size = fread(inbuf, 1, INBUF_SIZE, ifile);
		std::cout << "size " << data_size << std::endl;
		if (!data_size) break;
		dec.decode(inbuf, data_size);
	}

	dec.flush();

}

//文件(h264->yuv420)、相机(mjpeg-yuvj422->yuv420)解码
int video_capture_test() {
		AVCodecID a; //7 AV_CODEC_ID_MJPEG
		AVPixelFormat b; //13 AV_PIX_FMT_YUVJ422P

		VideoCapture capture;
		//if (!decoder.Load("D:/data/ws/cpp/_res/video.h264")) {
		//	decoder.Release();
		//	return 0;
		//}

		if (!capture.LoadCam("video=HD USB Camera")) {
		    capture.Release();
		    return 0;
		}

		capture.DumpVideoInfo();

		AVPixelFormat format = capture.GetPixelFormat();
		std::cout << "format: " << format << std::endl;
		if (format != AV_PIX_FMT_YUV420P) {}

		//SdlWindow::GetInstance()->Show("FFMPEG PLAYER", decoder.GetVideoWidth(), decoder.GetVideoHeight());
		AVFrame* frame;
		CVHelper cvhelper;
		while ((frame = capture.NextFrame()) != NULL) {
			//if (!SdlWindow::GetInstance()->Update(frame->data, frame->linesize)) {
			//    break;
			//}
			//std::cout << decoder.GetVideoWidth() << std::endl;
			cvhelper.show(frame);
		}

		capture.Release();
		return 0;
}

//实现各种封装格式转换
#include<iostream>
#include<libavutil/timestamp.h>
#include<libavformat/avformat.h>

int main00(int argc, char* argv[])
{
	//输出格式
	const AVOutputFormat* ofmt = NULL;
	//输入于输出媒体句柄。
	AVFormatContext* ifmt_ctx = NULL, * ofmt_ctx = NULL;
	AVPacket* pkt = NULL;
	//输入输出文件名字。
	const char* in_filename, * out_filename;
	in_filename = "D:/data/ws/cpp/_demo/x64/Release/test.mkv";
	out_filename = "D:/data/ws/cpp/ConsoleApplication/out/test1.mkv";
	int ret = -1;
	//用于存储输入流的索引。
	int* stream_mapping = NULL;
	int stream_index = 0;
	int frame_index = 0;

	//if (argc < 2)
	//{
	//	std::cout << "请键入输入文件和输出文件参数重新执行该程序！" << std::endl;
	//	return -1;
	//}
	//in_filename = argv[1];
	//out_filename = argv[2];

	//打开源文件。
	ret = avformat_open_input(&ifmt_ctx, in_filename, 0, 0);
	if (ret < 0)
	{
		std::cout << "could not open the filename :" << in_filename << std::endl;
		goto end;
	}
	else {
		std::cout << "open the filename :" << in_filename << " success" << std::endl;
	}
	ret = avformat_find_stream_info(ifmt_ctx, 0);
	if (ret < 0) {
		std::cout << "avformat_find_stream_info filed!" << std::endl;
		goto end;
	}
	//打印详细信息。
	//av_dump_format(ifmt_ctx, 0, in_filename, 0);

	stream_mapping = new int[ifmt_ctx->nb_streams];
	//为输出的media handle申请空间
	avformat_alloc_output_context2(&ofmt_ctx, NULL, NULL, out_filename);
	if (!ofmt_ctx) {
		std::cout << "Could not create output context!" << std::endl;
		avformat_close_input(&ifmt_ctx);
		goto end;
	}
	ofmt = ofmt_ctx->oformat;
	for (int i = 0; i < ifmt_ctx->nb_streams; i++)  //nb_streams==2;
	{
		//根据输入流创建输出流（Create output AVStream according to input AVStream）
		AVStream* in_stream = ifmt_ctx->streams[i];
		AVCodecParameters* in_codecpar = in_stream->codecpar;
		AVStream* out_stream = NULL;
		if (in_codecpar->codec_type != AVMEDIA_TYPE_AUDIO &&
			in_codecpar->codec_type != AVMEDIA_TYPE_VIDEO)
		{
			//这里将丢弃的流值存为-1,方便后续读帧时丢弃。
			stream_mapping[i] = -1;
			continue;
		}
		//将要保留的媒体流进行索引存储。
		stream_mapping[i] = stream_index;
		stream_index++;
		//新建一个媒体流
		out_stream = avformat_new_stream(ofmt_ctx, NULL);
		if (!out_stream)
		{
			std::cout << "avformat_new_stream failed" << std::endl;
			avformat_close_input(&ifmt_ctx);
			goto end;
		}
		//参数拷贝
		ret = avcodec_parameters_copy(out_stream->codecpar, in_codecpar);
		if (ret < 0)
		{
			std::cout << "avcodec_parameters_copy filed!" << std::endl;
			goto end;
		}
		out_stream->codecpar->codec_tag = 0;
	}
	//输出一下格式------------------
	av_dump_format(ofmt_ctx, 0, out_filename, 1);
	//打开输出文件（Open output file）
	if (!(ofmt->flags & AVFMT_NOFILE)) {
		ret = avio_open(&ofmt_ctx->pb, out_filename, AVIO_FLAG_WRITE);
		if (ret < 0) {
			std::cout << "could not open the outputfile:" << out_filename << std::endl;
			goto end;
		}
	}
	//写文件头（Write file header）
	ret = avformat_write_header(ofmt_ctx, NULL);
	if (ret < 0) {
		std::cout << "avformat_write_header failed!" << std::endl;
		goto end;
	}
	pkt = av_packet_alloc();
	while (1)
	{
		AVStream* in_stream = NULL;
		AVStream* out_stream = NULL;
		//获取pkt
		ret = av_read_frame(ifmt_ctx, pkt);
		if (ret < 0)
		{
			std::cout << "=== read the end of the file！" << std::endl;
			break;
		}
		in_stream = ifmt_ctx->streams[pkt->stream_index];
		if (stream_mapping[pkt->stream_index] < 0) {
			av_packet_unref(pkt);
			continue;
		}
		pkt->stream_index = stream_mapping[pkt->stream_index];
		/*
		out_stream = ofmt_ctx->streams[pkt->stream_index];
		//copy packet
		//时间基数转换
		//转换PTS/DTS（Convert PTS/DTS）
		pkt->pts = av_rescale_q_rnd(pkt->pts, in_stream->time_base, out_stream->time_base, (AVRounding)(AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX));
		pkt->dts = av_rescale_q_rnd(pkt->dts, in_stream->time_base, out_stream->time_base, (AVRounding)(AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX));
		pkt->duration = av_rescale_q(pkt->duration, in_stream->time_base, out_stream->time_base);
		//std::cout << pkt->pts << "=" << pkt->dts << "=" << pkt->duration << std::endl;
		//std::cout << pkt->stream_index << "=" << pkt->flags << "=" << pkt->size<< std::endl;
		pkt->pos = -1;
		*/

		//写入（Write）
		ret = av_interleaved_write_frame(ofmt_ctx, pkt);
		if (ret < 0) {
			std::cout << "av_interleaved_write_frame failed!" << std::endl;
			break;
		}
		frame_index++;
		if (frame_index % 1000 == 0)
		{
			std::cout << "Write frames to output file:" << frame_index << std::endl;
		}
		//记得引用计数减1,否则会引起内存泄漏。
		av_packet_unref(pkt);
	}
	//写文件尾（Write file trailer）

end:
	delete[] stream_mapping;
	avformat_close_input(&ifmt_ctx);
	av_write_trailer(ofmt_ctx);
	av_packet_free(&pkt);
	avformat_close_input(&ifmt_ctx);
	if (ofmt_ctx && !(ofmt->flags & AVFMT_NOFILE))
		avio_close(ofmt_ctx->pb);
	avformat_free_context(ofmt_ctx);
	if (ret < 0 && ret != AVERROR_EOF) {
		printf("Error occurred.\n");
		return -1;
	}
	system("pause");
	return 0;
}