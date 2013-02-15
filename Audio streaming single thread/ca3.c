/*
 ============================================================================
 Name        : ca3.c
 Author      : Srikanth Ayalasomayajulu
 Version     : 1.0
 Copyright   : Your copyright notice
 Description : Audio Streaming design in C.
 ============================================================================
 */

#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
/*
 * @includes "audstrefw2.h" header
 */
#include "audstrfw2.h"

/*
 * @defines maximum buffer for storage of packets and converted blocks
 */
#define max_pck 200
#define max_blk 20
#define max_decode 10

/*
 * Initialization and Declaration of packet and block arrays with their respective buffers
 */
PACKET packets[max_pck],*ptr;
BLOCK  block[max_blk];
/*
 * Initialization and Declaration of pck_counter for counting incoming packets
 */
int pck_counter=0, pck_tobe_decode=0;
/*
 * Initialization and Declaration of "decode_block" for counting decoded blocks
 */
int decode_block=0;
/*
 * Initialization and Declaration of "recv_pck" to count receiving packets
 */
int recv_pck=0;
/*
 * Initialization and Declaration of "check_decode" to count decoding blocks
 */
int check_decode=0;
/*
 * Initialization and Declaration of "check_block" to count played blocks
 */
int check_block=0;
/*
 * Initialization and Declaration of "blk_counter" & "local_flag" to count processing blocks and processed packets
 */
int blk_counter=0;
int local_flag=9;
/*
 * Initialization and Declaration of "count" & "blk_ply" to count played packets
 */
int count=0,blk_ply=0,blk_tobe_play=0;

/*
 * First method "pck_collec"  implementation for Packet collection
 */
void pck_collec(){

       		if(isPacketAvailable()){
       			packets[pck_counter] = getPacket();
       			pck_counter++;
       			pck_tobe_decode++;
       			if(pck_counter>=max_pck)
       			pck_counter=0;
       		}
       	}
/*
 * Second method "pck_process" implementation for processing packets and decoding of blocks
 */

void pck_process(){

	BLOCK temp;
					if((pck_counter/max_decode>=1) && (pck_counter%max_decode==0)){
					ptr = &packets[local_flag];
					if(isBlockSyncPacket(*ptr)){
					decodeAudioStep(ptr-9,0);
					decodeAudioStep(ptr-9,1);
					pck_collec();
					decodeAudioStep(ptr-9,2);
					pck_collec();
					decodeAudioStep(ptr-9,3);
					pck_collec();
					temp = decodeAudioStep(ptr-9,4);
					if(!(temp == ASBLOCK_ERROR)){
					block[blk_counter]= temp;
					*ptr=-1;
					blk_counter++;
					check_decode++;
					local_flag+=10;
					pck_tobe_decode-=10;
					blk_tobe_play++;
				   }
				}
			else{
				local_flag++;
			}
		if(local_flag>=max_pck)
		   local_flag=9;
		if(blk_counter>=max_blk)
		   blk_counter=0;
		}
}
/*
 * Third thread "pck_play" implementation for playing of decoded blocks
 */
void blk_play(){
	BLOCK temp;
			if(check_decode>15)	{
				if(!isBlockBufferFull() || !isBlockPlaying()){
					temp = block[count];
					if(!(temp == ASBLOCK_ERROR)){
					playBlock(temp);
					block[count]=-1;
					blk_tobe_play--;
					count++;
					blk_ply++;
					check_block++;
					   }
					}
			if(count>=max_blk)
			  count=0;
			}
	}

int main(void){

     initASFramework();
     while(!endOfPackets()){
     pck_collec();
     pck_process();
     blk_play();
     }
    while(pck_tobe_decode>=10 || blk_tobe_play>0){
    	 pck_process();
    	 blk_play();
     }
     shutdownASFramework();
  }
