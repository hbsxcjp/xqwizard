#include <windows.h>

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow) {
  ShellExecute(NULL, NULL,
      MessageBox(NULL, "�O�_�q SourceForge.net �U���H�ѧŮv�w�˥]?", "�w��ϥζH�ѧŮv", MB_ICONQUESTION + MB_YESNO) == IDYES ?
      "http://nchc.dl.sourceforge.net/sourceforge/xqwizard/xqwizard_trad.exe" :
      "http://www.elephantbase.net/xqwizard/download.htm", NULL, NULL, SW_SHOW);
  return 0;
}