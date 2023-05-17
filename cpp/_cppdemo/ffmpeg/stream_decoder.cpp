#include "stream_decoder.h"

stream_decoder::stream_decoder() :
	codec(NULL),
	avcodecCtx(NULL),
	parser(NULL),
	packet(NULL),
	frame(NULL),
	ret(-1)
{}

int stream_decoder::load(OnDecodeSuccess callback, AVCodecID codec_id)
{
	this->callback = callback;

	//const char* iFileName = "D:/data/ws/cpp/_res/test.h264";
	//FILE* ifile = fopen(iFileName, "rb");
	//if (ifile == NULL)
	//    return -1;
	//const char* oFileName = "D:/data/ws/cpp/_res/test.yuv";
	//FILE* ofile = fopen(oFileName, "wb");
	//if (ifile == NULL)
	//    ofile - 1;

	codec = avcodec_find_decoder(codec_id);
	avcodecCtx = avcodec_alloc_context3(codec);
	if (avcodec_open2(avcodecCtx, codec, nullptr) < 0) {
		return -1;
	}
	parser = av_parser_init(codec->id);
	if (!parser) {
		fprintf(stderr, "parser not found\n");
		return -1;
	}
	frame = av_frame_alloc();
	if (!frame) {
		fprintf(stderr, "Could not allocate video frame\n");
		return -1;
	}
	packet = av_packet_alloc();
}

int stream_decoder::flush()
{
	/* flush the decoder */
	CH::decode(avcodecCtx, nullptr, callback);

	//fclose(ifile);
	//fclose(ofile);s

	av_parser_close(parser);
	avcodec_free_context(&avcodecCtx);
	av_frame_free(&frame);
	av_packet_free(&packet);

	return 0;
}

int stream_decoder::decode(uint8_t* data, size_t data_size)
{
	/* use the parser to split the data into frames */
	//data = inbuf; //raw+64
	while (data_size > 0) {
		ret = av_parser_parse2(parser, avcodecCtx, &packet->data, &packet->size,
			data, data_size, AV_NOPTS_VALUE, AV_NOPTS_VALUE, 0);
		std::cout << "ret " << ret << std::endl;
		if (ret < 0) {
			fprintf(stderr, "Error while parsing\n");
			exit(1);
		}
		data += ret;
		data_size -= ret;
		std::cout << "packet size " << packet->size << std::endl;
		if (packet->size) {
			CH::decode(avcodecCtx, packet, callback);
		}
	}
	return 0;
}
