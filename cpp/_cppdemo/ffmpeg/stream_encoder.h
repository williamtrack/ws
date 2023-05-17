#pragma once

#include <iostream>
#include <thread>

extern "C" {
#include <libavformat/avformat.h>
#include <libavdevice/avdevice.h>
#include <libavutil/time.h>
}

#include "codecimpl.h"
#include "CVHelper.h"
#include "global.h"

class stream_encoder
{
public:
	stream_encoder();
	int load(OnEncodeSuccess callback, AVCodecID codec_id, int width, int height);
	int flush();
	int encode(AVFrame *avframe);
private:
	AVCodec* codec;
	AVCodecContext* pCodecCtx;
	//AVFormatContext* mFormatContext; //input file
	AVCodecParserContext* parser;
	AVPacket* packet;
	AVFrame* frame;
	//uint8_t inbuf[INBUF_SIZE + AV_INPUT_BUFFER_PADDING_SIZE];
	int ret;
	OnEncodeSuccess callback;

	void setPara(AVCodecContext** enc_ctx, AVCodecID codecId, int width, int height);
};

