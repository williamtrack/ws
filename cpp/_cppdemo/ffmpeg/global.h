
#ifndef GLOBAL_H_H_
#define GLOBAL_H_H_

extern "C"{
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavfilter/buffersink.h>
#include <libavfilter/buffersrc.h>
#include <libavutil/time.h>
#include <libavutil/opt.h>
#include <libavutil/imgutils.h>
#include <libavutil/channel_layout.h>
#include <libavutil/samplefmt.h>
#include <libswresample/swresample.h>
#include <libavutil/audio_fifo.h>
}

static char av_error[AV_ERROR_MAX_STRING_SIZE];
#define av_err2str(errnum) av_make_error_string(av_error, AV_ERROR_MAX_STRING_SIZE, errnum)

void InitVideoAVCodecCtx(AVCodecContext* c, AVCodecID codecId, int width, int height);

int InitABufferFilter(AVFilterGraph* filterGraph, AVFilterContext** filterctx, const char* name,
    AVRational timebase, int samplerate, AVSampleFormat format, uint64_t channel_layout);

int InitABufferSinkFilter(AVFilterGraph* filterGraph, AVFilterContext** filterctx, const char* name,
    AVSampleFormat format, int samplerate, uint64_t channel_layout);

uint8_t* getNulu(const uint8_t* source, uint64_t& start_pos, uint64_t end_pos, uint64_t& nulu_size);

#endif