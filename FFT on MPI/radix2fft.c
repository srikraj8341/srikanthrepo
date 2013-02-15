/*
 ============================================================================
 Name        : radix2fft.c
 Author      : Srikanth Ayalasomayajulu
 Matric No.	 : A0077017H
 Version     : 1.0
 Copyright   : Your copyright notice
 Description : New Parallel Algorithms 1D radix2 FFT in C
 ============================================================================
 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/time.h>
#include <math.h>
#include <mpi.h>
#include <complex.h>
#include <sys/resource.h>

	long int N;

	/* generation of inputs*/

	long int reverse(long int x) {
	 long int h = 0;
	 long int i = 0;
	 for(h = i = 0; i < log2(N); i++) {
	  h = (h << 1) + (x & 1);
	  x >>= 1;
	 }
	 return h;
	}
long int main(int argc, char* argv[]){
	N=(argc==2)?atoi(argv[1]):1024;
	complex float input[N];
	int  my_rank; /* rank of process */
	int  p;       /* number of processes */
	struct timeval start, stop;
	double total = 0;
	long int d,j,r,l,m=0,t=0,v,tt=0;
	complex float w = -I*(2*(3.1415926535897931)/N);
	//complex float W;
	float q;
	//int count=0;
	MPI_Status status ; /* return status for receive */
	MPI_Init(&argc, &argv);
	/* find out process rank */
	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);
	/* find out number of processes */
	MPI_Comm_size(MPI_COMM_WORLD, &p);
	complex float *ptr,*buff, c[N/p];
	ptr = c;
	buff = (complex float *) malloc(N*sizeof(complex float));

	for(long int i=0;i<N;i++){
		input[i]=i;
	}

		for(long int k=0;k<=(N/p-1);k++){
			gettimeofday(&start, 0);
				d=my_rank*N/p+k;
			  /*Reverse (d) returns the decimal
				number represented by d’s bit-reverse*/
				j=reverse(d)/(N/p);
				r=reverse(d)%(N/p);
				c[r]= input[my_rank*N/p+k];
				gettimeofday(&stop, 0);
				total += (stop.tv_sec+stop.tv_usec*1e-6)-(start.tv_sec+start.tv_usec*1e-6);

				if (j!=my_rank)
				{
				/*  Send the k-th data in Pi to Pj’s element c[r]
				 */
				MPI_Send(&r, 1, MPI_LONG_INT, j,0, MPI_COMM_WORLD);
				MPI_Send(&c[r], 1, MPI_C_FLOAT_COMPLEX, j,0, MPI_COMM_WORLD);
				}
				else{
				*(buff + r) = c[r];
				}
			}


		for (int k=0;k<p;k++)
			{
			if (k!=my_rank)
				{
				/*  Receive the k-th data from Pi to Pj’s element c[r]
				 */
				for(long int i=0;i<N/pow(p,2);i++){
			MPI_Recv(&r,1,MPI_LONG_INT,k,0,MPI_COMM_WORLD,&status);
			MPI_Recv(buff+r,1,MPI_C_FLOAT_COMPLEX,k,0,MPI_COMM_WORLD,&status);
					}
				}
				}

			gettimeofday(&start, 0);
			for(long int e=0;e<=(log2(N/p)-1);e++){

				l = pow(2,e);
				q=N/(2*l);

				for(long int k=0;k<=(N/p-1);k++){
					/* Computing the FFT on the individual processors without any communication
					 * with other processors
					 */

					if((my_rank*N/p+k)%l==(my_rank*N/p+k)%(2*l)){
						m=(my_rank*N/p+k)%l;
						complex float cemp1,cemp2;
						cemp1 = *(buff+k)+(*(buff+k+l))*(cexpf(w*q*m));
						cemp2 = *(buff+k)-(*(buff+k+l))*(cexpf(w*q*m));
						*(buff+k)=cemp1;
						*(buff+k+l)=cemp2;
						}
					}
			}
			gettimeofday(&stop, 0);
			total += (stop.tv_sec+stop.tv_usec*1e-6)-(start.tv_sec+start.tv_usec*1e-6);

			/*
			 * The second phase from step log(n/p) to step
			 * logn-1 requires communication between pairs of
			 * processors
			 *
			 */
			j=log2(p)+1;
			for(long int e=0;e<=(log2(p)-1);e++){
				t= pow(2,e);
				long int temp1 = e+log2(N/p);
				l=pow(2,temp1);
				q=N/(2*l);
				j=j-1;
				v=pow(2,j);
				/*
				 * send the data stored in present processor
				 * to the (i-p/v)-th processor.
				 */
				if((my_rank-p/v)>=0){
				if((my_rank-(p/v))%t==(my_rank-(p/v))%(2*t)){
					MPI_Send(buff, N/p, MPI_C_FLOAT_COMPLEX, (my_rank-(p/v)),0, MPI_COMM_WORLD);
							}
						}
				/*
				 * Receive the data block from the tt-th
				 * processor and store them into buff[n/v] to buff[n/v+n/p-1]
				 *
				 */
					if((my_rank)%t==(my_rank)%(2*t)){
					tt= my_rank+(p/v);
					   MPI_Recv(buff+N/v,N/p,MPI_C_FLOAT_COMPLEX,tt,0,MPI_COMM_WORLD,&status);
					 	gettimeofday(&start, 0);
					 	// computes the fft.
					for(long int k=0;k<=(N/p-1);k++){
						m=(my_rank*N/p+k)%l;
						long int value = N/v;
						complex float cemp3,cemp4;
						cemp3= *(buff+k)+(*(buff+k+value))*(cexpf(w*q*m));
						cemp4 = *(buff+k)-(*(buff+k+value))*(cexpf(w*q*m));
						*(buff+k) =cemp3;
						*(buff+k+value)=cemp4;
						}
						gettimeofday(&stop, 0);
						total += (stop.tv_sec+stop.tv_usec*1e-6)-(start.tv_sec+start.tv_usec*1e-6);
						/*
						 * Send the data block from the tt-th
						 * processor and store them into buff[n/v] to buff[n/v+n/p-1]
						 *
						 */
						MPI_Send(buff+N/v, N/p, MPI_C_FLOAT_COMPLEX,tt,0, MPI_COMM_WORLD);
						}
					/*
					 * receive the data from the (i-p/v)-th processor
					 * and store them in the buffer.
					 *
					 */
					if(my_rank-(p/v)>=0){
						if((my_rank-(p/v))%t==(my_rank-(p/v))%(2*t)){
							long int ttp = my_rank-(p/v);
							MPI_Recv(buff, N/p, MPI_C_FLOAT_COMPLEX,ttp,0, MPI_COMM_WORLD,&status);

						  }
						}
					}

			//printf("%.6f\n",total);
			for(long int b=0;b<=(N/p-1);b++){
			printf("my rank : %d, k :%d, c[%d] : %f+i%f\n",my_rank,b,b,creal(*(buff+b)),cimag(*(buff+b)));
			}

	/* shut down MPI */
	MPI_Finalize();
	return 0;

}
