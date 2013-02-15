/*
 ============================================================================
 Name        : ca2.c
 Author      : Srikanth Ayalasomayajulu
 Version     : 1.0
 Copyright   : Your copyright notice
 Description : Audio Streaming design in C, Ansi-style
 ============================================================================
 */

#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
/*
 * @includes "audstrefw.h" header
 */
#include "audstrfw.h"

/*
 * @defines maximum buffer for storage of packets and converted blocks
 */
#define max_pck 500
#define max_blk 50

/*
 * Initialization of mutex( Mutual Exclusion) to achieve synchronization
 */
pthread_mutex_t mutexip= PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutexproc = PTHREAD_MUTEX_INITIALIZER;

/*
 * Initialization and Declaration of packet and block arrays with their respective buffers
 */
PACKET packets[max_pck];
BLOCK  block[max_blk];
/*
 * Initialization and Declaration of pck_counter for counting incoming packets
 */
int pck_counter=0;
/*
 * Initialization and Declaration of "decode_block" for counting decoded blocks
 */
int decode_block=0;
/*
 * Initialization and Declaration of "recv_pck" to count receiving packts
 */
int recv_pck=0;

/*
 * First thread "pck_collec"  implementation for Packet collection
 */
void *pck_collec(void *tr){
    while(endOfPackets()==0){
       		if(isPacketAvailable()){
       			pthread_mutex_lock( &mutexip );
       			packets[pck_counter] = getPacket();
       			pck_counter++;
       			recv_pck++;
       			pthread_mutex_unlock( &mutexip );
       		}
       		pthread_mutex_lock( &mutexip );
       		if(pck_counter>=max_pck)
       			pck_counter=0;
       		pthread_mutex_unlock( &mutexip );
    }
     pthread_exit(NULL);
}
/*
 * Second Thread "pck_process" implementation for processing packets and decoding of blocks
 */

void *pck_process( void *tr ){

	int check_decode=10,local_buffer[10],local_flag=9,temp_count=0,wait=0,j, blk_counter=0,x=0;
	BLOCK temp;
		while(!endOfPackets()){
			pthread_mutex_lock( &mutexip);
			temp_count=recv_pck;
			pthread_mutex_unlock( &mutexip);
			while(temp_count<check_decode){
				pthread_mutex_lock( &mutexip);
				temp_count=recv_pck;
				pthread_mutex_unlock( &mutexip);
				usleep(1000);
						}
						pthread_mutex_lock( &mutexip);
							for(j=0;j<10;j++){
								local_buffer[j]=packets[j+local_flag-9];
								}
						pthread_mutex_unlock( &mutexip);
						check_decode+=10;

						if(isBlockSyncPacket(local_buffer[9])){
						temp=decodeAudio(local_buffer);
						pthread_mutex_lock(&mutexproc);
						block[blk_counter]= temp;
						pthread_mutex_unlock(&mutexproc);

					if(!(temp =="ASBLOCK_ERROR")){
					pthread_mutex_lock(&mutexproc);
					blk_counter++;
					decode_block++;
					pthread_mutex_unlock(&mutexproc);

				}
			}
		local_flag+=10;
		if(local_flag>=max_pck)
			local_flag=9;
		if(blk_counter>=max_blk)
			blk_counter=0;

		}
		pthread_exit(NULL);
	  }
/*
 * Third thread "pck_play" implementation for playing of decoded blocks
 */
void *blk_play(void *tr){
	int local_blk=0, count=0,blk_ply=0;
	BLOCK temp;

	pthread_mutex_lock( &mutexproc);
	local_blk=decode_block;
	pthread_mutex_unlock( &mutexproc);
	/*
	 * Sleep of 2ms introduced to avoid audio breaks
	 */
	while(local_blk<10){
		usleep(20000);
		pthread_mutex_lock( &mutexproc);
		local_blk=decode_block;
		pthread_mutex_unlock( &mutexproc);

	}
		while(!endOfPackets()){
			pthread_mutex_lock( &mutexproc);
			local_blk=decode_block;
			pthread_mutex_unlock( &mutexproc);
			if(!isBlockPlaying() ||!isBlockBufferFull() ){
				pthread_mutex_lock( &mutexproc);
					temp = block[count];
				pthread_mutex_unlock( &mutexproc);
					playBlock(temp);
					count++;
					blk_ply++;
					}
			if(count>=max_blk)
				count=0;
	}
	pthread_exit(NULL);
}

int main(void){
    pthread_t ipthrd, procthrd, opthrd;
    char *ipmsg = "packets available";
    char *procmsg = "packets processing";
    char *playmsg = "play audio";
    int create;
    initASFramework();
    create = pthread_create(&ipthrd, NULL,pck_collec,(void*) ipmsg);
    if (create)
    	{
    		printf("packet collection not created :%d\n",create);
    		return -1;
    	}
    
    create = pthread_create(&procthrd, NULL,pck_process,(void*)procmsg);
    if (create)
    	{
    		printf("packet process not created :%d\n",create);
    		return -1;
    	}

    create = pthread_create(&opthrd, NULL,blk_play, (void*)playmsg);
    if (create)
    	{
    		printf("block play thread not created :%d\n",create);
    		return -1;
    	}

    pthread_join(ipthrd,NULL);
    pthread_join(procthrd,NULL);
    pthread_join(opthrd, NULL);
    shutdownASFramework();
    pthread_exit(NULL);
}
