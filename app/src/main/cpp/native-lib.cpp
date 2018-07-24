#include <jni.h>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>

using namespace std;
using namespace cv;

void modifyMatBoundaries(JNIEnv *env, Mat &mat, jintArray hsvBounds);
extern "C"
JNIEXPORT void JNICALL
Java_com_gl_unawa_listeners_CVListener_modifyMat(JNIEnv *env, jobject instance,
                                        jlong matAddr, jintArray hsvBounds) {

    Mat &mat = *(Mat *) matAddr;
//    cvtColor(mat, mat, COLOR_BGR2GRAY);
//    cv::Canny(mat, mat, 100, 3, 3);
//    modifyMatBoundaries(env, mat, hsvBounds);

}

void modifyMatBoundaries(JNIEnv *env, Mat &mat, jintArray hsvBounds) {
    cvtColor(mat, mat, COLOR_BGR2HSV);
    jint* arr = env->GetIntArrayElements(hsvBounds, 0);
    Mat mask;
    inRange(mat, Scalar(arr[0], arr[2], arr[4]), Scalar(arr[1], arr[3], arr[5]), mat);
}