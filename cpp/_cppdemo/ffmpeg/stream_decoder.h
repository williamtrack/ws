#pragma once
//分段数据解码器

#include <iostream>
#include <thread>

extern "C" {
#include <libavformat/avformat.h>
#include <libavdevice/avdevice.h>
#include <libavutil/time.h>
}

#include "codecimpl.h"
#include "CVHelper.h"

#define INBUF_SIZE 4096

class stream_decoder
{
public:
	stream_decoder();
	int load(OnDecodeSuccess callback, AVCodecID codec_id);
	int flush();
	int decode(uint8_t* data, size_t data_size);
private:
	AVCodec* codec;
	AVCodecContext* avcodecCtx;
	//AVFormatContext* mFormatContext; //input file
	AVCodecParserContext* parser;
	AVPacket* packet;
	AVFrame* frame;
	//uint8_t inbuf[INBUF_SIZE + AV_INPUT_BUFFER_PADDING_SIZE];
	int ret;
	OnDecodeSuccess callback;
};

