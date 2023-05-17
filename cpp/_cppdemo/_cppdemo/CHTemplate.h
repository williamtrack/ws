#pragma once
class CHTemplate
{
public:
	void test();
	template <typename T> T compare(const T& v1, const T& v2);
	template <typename T,typename U> U compare(const T& v1, const T& v2,const U &v3, const U& v4);
};

template<typename T>
inline T CHTemplate::compare(const T& v1, const T& v2)
{
	if (v1 < v2)return v1;
	if (v2 < v1)return v2;
	return 0;
}

template<typename T, typename U>
inline U CHTemplate::compare(const T& v1, const T& v2,const U& v3, const U& v4)
{
	if (v1 < v2)return v3;
	if (v2 < v1)return v4;
	return 'e';
}