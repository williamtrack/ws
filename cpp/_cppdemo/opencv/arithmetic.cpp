#include "arithmetic.h"

using namespace cv;
Vec3d rainbow(int length, int i) { //²Êºç½¥±äËã·¨

	Vec3d temp;
	int r, g, b;
	if (i < length / 3) {
		r = 255;
		g = ceil(255 * 3 * i / length);
		b = 0;
	}
	else if (i < length / 2) {
		r = ceil(750 - i * (250 * 6 / length));
		g = 255;
		b = 0;
	}
	else if (i < length * 2 / 3) {
		r = 0;
		g = 255;
		b = ceil(i * (250 * 6 / length) - 750);
	}
	else if (i < length * 5 / 6) {
		r = 0;
		g = ceil(1250 - i * (250 * 6 / length));
		b = 255;
	}
	else {
		r = ceil(150 * i * (6 / length) - 750);
		g = 0;
		b = 255;
	}
	temp[0] = b;
	temp[1] = g;
	temp[2] = r;
	return temp;
}