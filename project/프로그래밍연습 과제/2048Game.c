#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <termios.h>

#define MAX_RANK 50


typedef struct{
  char name[10];
  char s_f[8];
  int ncombo;
  int nmove;
  int time;
}Rank;
Rank rank[MAX_RANK];

int ch;
int a[5][5]={0};
int y,t,moving,c,comb;
static int r;
clock_t start, end;
int menu();
void game();
void game_play(int(*a)[5]);
void how_to_game();
void ranking();
int check(int(*a)[5]);
int combo(int y);
int time_attack();
void screen(int(*a)[5]);
int move();
void save_rank(int m);
void sorting(FILE* fp,int n);
int max_combo(int com);
int check_time(int t);
int kbhit();


int main(void) {
  int p;
  p=menu();
  switch(p){
    case 1:
    game();
    break;
    case 2:
    how_to_game();
    break;
    case 3:
    ranking();
    break;
    case 4:
    exit(1);}

  return 0;
}

int menu(){
  int p;
  printf("1.게임 시작\n2.게임 방법 소개\n3.랭크 보기\n4.나가기\n메뉴를 선택하시오:\n");
  scanf("%d",&p);
  getchar();
  return p;
}
void game(){
 
  int n1=0,n2=0,n3=0,n4=0, i=0;
  start=clock();
  srand(time(NULL));
  while ((n1==n3) && (n2==n4)){
  n1=rand()%5;
  n2=rand()%5;
  n3=rand()%5;
  n4=rand()%5;}
  a[n1][n2]=(rand()%2+1)*2;
  a[n3][n4]=(rand()%2+1)*2;
  game_play(a);
}

void game_play(int (*a)[5]){
 int n1,n2;
 int i,j,k,m=1,x,z;
 do {
 r=0;
 do{screen(a);
}while(t!=0 && !kbhit());
 c=ch;
 if (t==0)
 {m=0;
 goto A;}
 else if (c=='w')
 {y=0;
  moving=move();
   for(j=0;j<5;j++){
     for (i=0;i<5;i++){
       for (k=0;k<4;k++){
       if (a[k][j]==0){
       a[k][j]=a[k+1][j];
       a[k+1][j]=0;}
       }}}
      for(j=0;j<5;j++){
      for (i=0;i<4;i++){
        if (a[i][j]==a[i+1][j] &&a[i][j]!=0){
          a[i][j]=a[i][j]+a[i][j];
          a[i+1][j]=0;
          y++;
          for (k=i+1;k<4;k++)
          {a[k][j]=a[k+1][j];}
        }
      } 
   }
 }
 else if (c=='d')
 {y=0;
 moving=move();
  for (j=4;j>=0;j--)
 {
   for(i=4;i>=0;i--){
     for(k=4;k>0;k--){
     if (a[j][k]==0){
       a[j][k]=a[j][k-1];
       a[j][k-1]=0;
     }}
   }
 }
 for(j=4;j>=0;j--){
 for(i=4;i>0;i--){
   if (a[j][i]==a[j][i-1]&&a[j][i]!=0){
     a[j][i]=a[j][i]+a[j][i];
     a[j][i-1]=0;
     y++;
     for(k=i-1;k>0;k--){
       a[j][k]=a[j][k-1];
     }
   }
 }}}
 else if (c=='s'){
   y=0;
   moving=move();
   for(j=4;j>=0;j--){
     for (i=4;i>=0;i--){
       for(k=4;k>0;k--){
       if (a[k][j]==0){
         a[k][j]=a[k-1][j];
         a[k-1][j]=0;
       }
     }}}
     for(j=4;j>=0;j--){
     for(i=4;i>0;i--){
       if(a[i][j]==a[i-1][j]&&a[i][j]!=0){
        a[i][j]=a[i][j]+a[i][j];
        a[i-1][j]=0;
        y++;
         for(k=i-1;k>0;k--){
           a[k][j]=a[k-1][j];
         }
       }
     }
   }
 }
 else if (c=='a'){
   y=0;
   moving=move();
   for(j=0;j<5;j++){
     for(i=0;i<5;i++){
       for(k=0;k<4;k++){
       if (a[j][k]==0){
         a[j][k]=a[j][k+1];
         a[j][k+1]=0;
       }
     }}
   }
   for(j=0;j<5;j++){
   for(i=0;i<4;i++){
     if(a[j][i]==a[j][i+1]&&a[j][i]!=0){
       a[j][i]=a[j][i]+a[j][i];
       a[j][i+1]=0;
       y++;
       for(k=i+1;k<4;k++){
         a[j][k]=a[j][k+1];
       }
     }
   }}}
 c=0;
 comb=combo(y);
 do {
  n1=rand()%5;
  n2=rand()%5;
  } while(a[n1][n2]!=0);

  a[n1][n2]=(rand()%2+1)*2;
  m=check(a);
}while(m==1);
A: if (m==0)
printf("GAME OVER\n");
else printf("GAME CLEAR\n");
end=clock();
save_rank(m);
}
void how_to_game(){
  printf("*HOW TO PLAY*\nUse your keys 'w,a,s,d' to move the tiles. When two tiles with the same number touch, they merge into one! If you make the 2048 tile, you clear the game!\n");
  sleep(5);
  system("clear");
}
void ranking(){
  int i;
  char line[255];
  FILE*fp;
  fp=fopen("text.txt","r");
  printf("%-6s           %-7s   %-10s    %-10s   %-s\n","이름","성공여부","콤보횟수","이동횟수","총 시간");
  printf("=========================================================\n");
  while(fgets(line,sizeof(line),fp)!=NULL){printf("%s",line);}
  fclose(fp);
}
int check_time(int t){
  if(t==0)
  return 0;
  else return 1;
}
int check(int(*a)[5]){

 int i,j;
 for (i=0;i<5;i++)
{
  for(j=0;j<5;j++){
    if (a[i][j]==0)
    return 1;
    else if (a[i][j]==2048)
    return 2048;}}

 for (i=0;i<4;i++)
 {
   for (j=0;j<5;j++){
     if(a[i][j]==a[i+1][j])
     return 1;
  }}
  for(i=0;i<4;i++){
    for(j=0;j<5;j++){
    if(a[j][i]==a[j][i+1])
    return 1;
  }}
  return 0;
}

int combo(int y){
static int com=0;
if (y==0)
com=0;
else com++;
max_combo(com);
return com;}

int max_combo(int com){

static int max=0;
if (max<com)
max=com;
return max;
}

int time_attack(){
 int b;
 b=10-r;
 r++;
 return b;
}
void screen(int(*a)[5]){
  int i;
  for (i=0;i<=4;i++)
  {printf(" %5s %5s %5s %5s %5s \n|%5d|%5d|%5d|%5d|%5d|\n","-----","-----","-----","-----","-----\n",a[i][0],a[i][1],a[i][2],a[i][3],a[i][4]);}
  printf(" ----- ----- ----- ----- ----- ");
  printf("\nCOMBO:%d",comb);
  printf("\nMOVE:%d",moving);
  t=time_attack();
  printf("\nTIME:%d\n",t);
  sleep(1);
  system("clear");
}
int move(){
  static int count=0;
  count++;
  return count;
}
void save_rank(int m){
  FILE*fp;
  int t1[8];
  int i=0,j;
  fp=fopen("text.txt","a+");
  printf("게임자의 이름을 적으세요.");
  while (*rank[i].name!=NULL){
  i++;}
  scanf("%s",rank[i].name);
  if (m==0)
  strcpy(rank[i].s_f,"FAIL");
  else if (m==2048)
  strcpy(rank[i].s_f,"SUCCESS");
  rank[i].ncombo=max_combo(0);
  rank[i].nmove=moving;
  rank[i].time=end=start;
  sorting(fp,i);
  fprintf(fp,"%-6s%-7s%-10d%-10d%d\n",rank[i].name,rank[i].s_f,rank[i].ncombo,rank[i].nmove,rank[i].time);
  
  fclose(fp);
}

void sorting(FILE*fp,int n){
int temp,i,j;
 for (i=0;i<=n;i++){
 for (j=0;j<n;j++){
   if (rank[j].ncombo>rank[j+1].ncombo){
     temp=rank[j].ncombo;
     rank[j].ncombo=rank[j+1].ncombo;
     rank[j+1].ncombo=temp;
   }
 }
 }
}

int kbhit(void){
  struct termios oldt, newt;
  
  int oldf;
  ch=EOF;
  tcgetattr(STDIN_FILENO,&oldt);
  newt=oldt;
  newt.c_lflag &= ~(ICANON | ECHO);
  tcsetattr(STDIN_FILENO, TCSANOW, &newt);
  oldf=fcntl(STDIN_FILENO, F_GETFL, 0);
  fcntl(STDIN_FILENO,F_SETFL,oldf|O_NONBLOCK);

  ch=getchar();

  tcsetattr(STDIN_FILENO,TCSANOW,&oldt);
  fcntl(STDIN_FILENO,F_SETFL,oldf);
  
   if(ch!=EOF){
   return 1;
  }
  return 0;
}
