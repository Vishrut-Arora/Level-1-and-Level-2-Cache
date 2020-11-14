// NAME: VISHRUT ARORA
// CACHE Level1 PROJECT

import java.util.*;
import java.io.*;
import java.lang.*;

public class cache_new{
	static int machine_word_length=32;
	static int m_rows=0;
	static int m_cols=0;
	static int c_rows=0;
	static int c_cols=0;
	static StringBuffer memory_array[][];
	static StringBuffer cache_array[][];
	static StringBuffer address_array[][];
	static StringBuffer tag_array[];//associative
	static int direct_address[];
	static int associativity_address[];
	static int associate_counter=0;
	static int num_set=0;
	static StringBuffer tag_bits[];//direct
	static StringBuffer tag_set[];
	static int lines_in_each_set=0;
	static int set_counter[];
	static int num_hits=0;
	static int num_miss=0;
	
	static long decimal_value(String n) 
	{ 
		String num = n; 
		long dec_value = 0; 

		long base = 1; 

		 int len = num.length();
		for (int i = len - 1; i >= 0; i--) { 
			if (num.charAt(i) == '1') 
				dec_value += base; 
			base = base * 2; 
		} 

		return dec_value; 
	} 
	// boolean data_present(StringBuffer data){
	// 	for(int i=0;i<c_rows;i++){
	// 		for(int j=0;j<c_cols;j++){
	// 			if((cache_array[i][j]+"").equals(data+"")){
	// 				return true;
	// 			}
	// 		}
	// 	}
	// 	return false;
	// }
	void Direct(int cache_lines,int block_size){
		memory_array=new StringBuffer[machine_word_length/block_size][block_size];
		cache_array=new StringBuffer[cache_lines][block_size];
		address_array=new StringBuffer[cache_lines][block_size];
		m_rows=machine_word_length/block_size;
		m_cols=block_size;
		c_rows=cache_lines;
		c_cols=block_size;
		tag_bits=new StringBuffer[cache_lines];
		int counter=0;
		for(int i=0;i<machine_word_length/block_size;i++){
			for(int j=0;j<block_size;j++){
				StringBuffer s2=new StringBuffer(counter+"");
				memory_array[i][j]=s2;
				++counter;
			}
		}
		for(int i=0;i<cache_lines;i++){
			for(int j=0;j<block_size;j++){
				StringBuffer s2=new StringBuffer("-1");
				cache_array[i][j]=s2;
				address_array[i][j]=s2;
			}
		}
	}
	void direct_write(int address,StringBuffer data){
System.out.println("--------------------\n     LEVEL 1 CACHE\n--------------------");
	
		int block_offset=(int)(Math.log(c_cols) / Math.log(2));
		int line_num=(int)(Math.log(c_rows) / Math.log(2));
		int tag=machine_word_length-line_num-block_offset;
		String str=String.format("%"+(machine_word_length)+"s", Integer.toBinaryString(address)).replace(' ', '0');
		StringBuffer tag2=new StringBuffer(decimal_value(str.substring(0,tag)+"")+"");

		int line_num2=Integer.parseInt(str.substring(tag,tag+line_num)+"",2);
		int block_offset2=Integer.parseInt(str.substring(tag+line_num,str.length())+"",2);
		
		int found=0;
		if(!((tag_bits[line_num2]+"").equals("null")) && !((tag_bits[line_num2]+"").equals(str.substring(0,tag)))){

			found=1;
		}
		else{

			found=0;
		}
		if(found==1){
			System.out.println("The block is replaced by Line Address : "+str.substring(tag,tag+line_num)+"\n                         Tag Address at : "+tag_bits[line_num2]);
		}
		StringBuffer str_add=new StringBuffer(""+str);
		//address_array[line_num2][block_offset2]=str_add;
		StringBuffer sts=new StringBuffer(str.substring(0,tag));
		tag_bits[line_num2]=sts;	
		cache_array[line_num2][block_offset2]=data;
		System.out.println("Tag: "+str.substring(0,tag)+"\nLine_number: "+str.substring(tag,tag+line_num)+"\nBlock_offset: "+str.substring(tag+line_num,str.length())+"\nData: "+cache_array[line_num2][block_offset2]);
		print_cache();
	
	}


	void direct_read(int address){
System.out.println("--------------------\n     LEVEL 1 CACHE\n--------------------");
		int block_offset=(int)(Math.log(c_cols) / Math.log(2));
		int line_num=(int)(Math.log(c_rows) / Math.log(2));
		int tag=machine_word_length-line_num-block_offset;
		String str=String.format("%"+(machine_word_length)+"s", Integer.toBinaryString(address)).replace(' ', '0');
		StringBuffer tag2=new StringBuffer(decimal_value(str.substring(0,tag)+"")+"");

		int line_num2=Integer.parseInt(str.substring(tag,tag+line_num)+"",2);
		int block_offset2=Integer.parseInt(str.substring(tag+line_num,str.length())+"",2);
		int found=0;
		if((tag_bits[line_num2]+"").equals(str.substring(0,tag))){
			System.out.println("Its a HIT");
			++num_hits;
			found=1;
		}
		else{
			System.out.println("Its a MISS");
            System.out.println("Address not found");			
			++num_miss;
		}
		if(found==1){	
		System.out.println("Tag: "+str.substring(0,tag)+"\nLine_number: "+str.substring(tag,tag+line_num)+"\nBlock_offset: "+str.substring(tag+line_num,str.length())+"\nData: "+cache_array[line_num2][block_offset2]);
	}
		print_cache();
	}
	void print_cache(){
		System.out.println("Hits: "+num_hits+" Miss: "+num_miss);
		System.out.print("                  ");
		for(int i=0;i<c_cols;i++){
			System.out.print("    BO:"+i+"   ");
		}
		System.out.println("");
		for(int i=0;i<c_rows;i++){
			System.out.print("Line_number: "+i+"-------- ");
			for(int j=0;j<c_cols;j++){
				if(cache_array[i][j].length()>1){
				System.out.print(cache_array[i][j]+"     |   ");
			}
			else{
			System.out.print(cache_array[i][j]+"      |   ");	
			}
			}
			System.out.println("");
		}

	}	
	void Associative(int cache_lines,int block_size){
		
		memory_array=new StringBuffer[machine_word_length/block_size][block_size];
		cache_array=new StringBuffer[cache_lines][block_size];
		address_array=new StringBuffer[cache_lines][block_size];
		m_rows=machine_word_length/block_size;
		m_cols=block_size;
		c_rows=cache_lines;
		c_cols=block_size;
		tag_array=new StringBuffer[c_rows];
		int counter=0;
		for(int i=0;i<machine_word_length/block_size;i++){
			for(int j=0;j<block_size;j++){
				StringBuffer s2=new StringBuffer(counter+"");
				memory_array[i][j]=s2;
				++counter;
			}
		}
		for(int i=0;i<cache_lines;i++){
			for(int j=0;j<block_size;j++){
				StringBuffer s2=new StringBuffer("-1");
				cache_array[i][j]=s2;
				address_array[i][j]=s2;
			}
		}
	}
	void associative_write(int address,StringBuffer data){
System.out.println("--------------------\n     LEVEL 1 CACHE\n--------------------");

		int block_offset=(int)(Math.log(c_cols) / Math.log(2));
		int tag=machine_word_length-block_offset;

		String str=String.format("%"+(tag+block_offset)+"s", Integer.toBinaryString(address)).replace(' ', '0');
		StringBuffer tag2=new StringBuffer(decimal_value(str.substring(0,tag)+"")+"");
		int block_offset2=Integer.parseInt(str.substring(tag,str.length())+"",2);
		if(associate_counter==c_rows){
			associate_counter=0;
		}

		int found=0;
		if(!((tag_array[associate_counter]+"").equals("null")) && !((tag_array[associate_counter]+"").equals(str.substring(0,tag)))){
			
System.out.println("The block is replaced by Tag Address at: "+tag_array[associate_counter]);			
			found=1;
		}
		else{
		
			found=0;
		}		

		StringBuffer str_add=new StringBuffer(""+str);
		address_array[associate_counter][block_offset2]=str_add;		
		cache_array[associate_counter][block_offset2]=data;
		StringBuffer sts=new StringBuffer(str.substring(0,tag));
		tag_array[associate_counter]=sts;
		++associate_counter;
		System.out.println("Tag: "+str.substring(0,tag)+"\nBlock_offset: "+str.substring(tag,str.length())+"\nData: "+data);		

		print_cache2();
	
	}
	void associative_read(int address){
System.out.println("--------------------\n     LEVEL 1 CACHE\n--------------------");		
		int block_offset=(int)(Math.log(c_cols) / Math.log(2));
		int tag=machine_word_length-block_offset;

		String str=String.format("%"+(tag+block_offset)+"s", Integer.toBinaryString(address)).replace(' ', '0');
		StringBuffer tag2=new StringBuffer(decimal_value(str.substring(0,tag)+"")+"");
		int block_offset2=Integer.parseInt(str.substring(tag,str.length())+"",2);
	
		int found=0;
		int index=-1;
    	for(int i=0;i<c_rows;i++){
    		if((tag_array[i]+"").equals(str.substring(0,tag)))
    		{
    			index=i;
    			found=1;
    			break;
    		}
    	}	
		if(found==1){
			System.out.println("Its a HIT");
			++num_hits;
		}
		else{
			System.out.println("Its a MISS");
            System.out.println("Address not found");			
			++num_miss;
		}
		if(found==1){
		System.out.println("Tag: "+str.substring(0,tag)+"\nBlock_offset: "+str.substring(tag,str.length())+"\nData: "+cache_array[index][block_offset2]);
		}
		
		print_cache2();
	}		

	void print_cache2(){
		System.out.println("Hits: "+num_hits+" Miss: "+num_miss);
		System.out.print("                  ");
		for(int i=0;i<c_cols;i++){
			System.out.print("    BO:"+i+"   ");
		}
		System.out.println("");
		for(int i=0;i<c_rows;i++){
			System.out.print("Line_number: "+i+"-------- ");
			for(int j=0;j<c_cols;j++){
				if(cache_array[i][j].length()>1){
				System.out.print(cache_array[i][j]+"     |   ");
			}
			else{
			System.out.print(cache_array[i][j]+"      |   ");	
			}
			}
			System.out.println("");
		}

	}
	void n_way_set(int cache_lines,int block_size,int n){
		
		memory_array=new StringBuffer[machine_word_length/block_size][block_size];
		cache_array=new StringBuffer[cache_lines][block_size];
		address_array=new StringBuffer[cache_lines][block_size];
		m_rows=machine_word_length/block_size;
		m_cols=block_size;
		c_rows=cache_lines;
		c_cols=block_size;
		num_set=cache_lines/n;
		tag_set=new StringBuffer[cache_lines];
		lines_in_each_set=n;
		set_counter=new int[num_set];
		int counter=0;
		for(int i=0;i<machine_word_length/block_size;i++){
			for(int j=0;j<block_size;j++){
				StringBuffer s2=new StringBuffer(counter+"");
				memory_array[i][j]=s2;
				++counter;
			}
		}
		for(int i=0;i<cache_lines;i++){
			for(int j=0;j<block_size;j++){
				StringBuffer s2=new StringBuffer("-1");
				cache_array[i][j]=s2;
				address_array[i][j]=s2;
			}
		}
	}
	void n_way_set_read(int address){
System.out.println("--------------------\n     LEVEL 1 CACHE\n--------------------");		
		int block_offset=(int)(Math.log(c_cols) / Math.log(2));
		int set_number=(int)(Math.log(num_set) / Math.log(2));
		int tag=machine_word_length-set_number-block_offset;
		String str=String.format("%"+(machine_word_length)+"s", Integer.toBinaryString(address)).replace(' ', '0');
		StringBuffer tag2=new StringBuffer(decimal_value(str.substring(0,tag)+"")+"");
		//int block_number2=Integer.parseInt(str.substring(0,block_number)+"",2);
		int block_offset2=Integer.parseInt(str.substring(tag+set_number,str.length())+"",2);
		int set_number2=0;
		if(set_number==0){
			set_number2=0;
		}
		else{
		set_number2=Integer.parseInt(str.substring(tag,tag+set_number)+"",2);
		}
		
		int found=0;
		int index=-1;
		for(int i=0;i<lines_in_each_set;i++){
			if((tag_set[lines_in_each_set*set_number2+i]+"").equals(str.substring(0,tag))){
				index=lines_in_each_set*set_number2+i;
				//System.out.println("Its a HIT");
				//++num_hits;
				found=1;
			}
		}
		if(found==1){
			
				System.out.println("It's a HIT");
				++num_hits;
			}
			else{
				found=0;
				System.out.println("It's a MISS");
				System.out.println("Address not found");
				++num_miss;
			}
		

		if(found==1){
		System.out.println("\nTag: "+str.substring(0,tag)+"\nSet_number: "+str.substring(tag,tag+set_number)+"\nBlock_offset: "+str.substring(tag+set_number,str.length())+"\nData: "+cache_array[index][block_offset2]);
		}
		print_cache3();
	}
	void n_way_set_write(int address,StringBuffer data){
System.out.println("--------------------\n     LEVEL 1 CACHE\n--------------------");		
	
		int block_offset=(int)(Math.log(c_cols) / Math.log(2));
		int set_number=(int)(Math.log(num_set) / Math.log(2));
		int tag=machine_word_length-set_number-block_offset;
		String str=String.format("%"+(machine_word_length)+"s", Integer.toBinaryString(address)).replace(' ', '0');
		StringBuffer tag2=new StringBuffer(decimal_value(str.substring(0,tag)+"")+"");
		//int block_number2=Integer.parseInt(str.substring(0,block_number)+"",2);
		int block_offset2=Integer.parseInt(str.substring(tag+set_number,str.length())+"",2);
		int set_number2=0;
		if(set_number==0){
			set_number2=0;
		}
		else{
		set_number2=Integer.parseInt(str.substring(tag,tag+set_number)+"",2);
		}	
		int flag=0;
		if(set_counter[set_number2]==lines_in_each_set){
			set_counter[set_number2]=0;
		}
		
		int found=0;
		if(!((tag_set[lines_in_each_set*set_number2+set_counter[set_number2]]+"").equals("null")) && !((tag_set[lines_in_each_set*set_number2+set_counter[set_number2]]+"").equals(str.substring(0,tag)))){
			//++num_hits;
		System.out.println("The block is replaced by Tag Address at : "+tag_set[lines_in_each_set*set_number2+set_counter[set_number2]]);			
			found=1;
		}
		else{
			//++num_miss;
			found=0;
		}	
		    StringBuffer str_add=new StringBuffer(""+address);			
			//address_array[lines_in_each_set*set_number2+set_counter][block_offset2]=str_add;
			cache_array[lines_in_each_set*set_number2+set_counter[set_number2]][block_offset2]=data;
			StringBuffer sts=new StringBuffer(str.substring(0,tag));
			tag_set[lines_in_each_set*set_number2+set_counter[set_number2]]=sts;
			++set_counter[set_number2];						

		System.out.println("\nTag: "+str.substring(0,tag)+"\nSet_number: "+str.substring(tag,tag+set_number)+"\nBlock_offset: "+str.substring(tag+set_number,str.length())+"\nData: "+data);

		print_cache3();
	}			


	void print_cache3(){
		System.out.println("Hits: "+num_hits+" Miss: "+num_miss);
		System.out.println("");
		for(int i=0;i<num_set;i++){
			System.out.print("    Set_Number: "+i+"---- ");
		for(int j=0;j<lines_in_each_set;j++){

				for(int k=0;k<c_cols;k++){
					if(cache_array[i*lines_in_each_set+j][k].length()>1){
				System.out.print("    "+cache_array[i*lines_in_each_set+j][k]+"  ");
			}
			else{
				System.out.print("     "+cache_array[i*lines_in_each_set+j][k]+"  ");
			}
			}
			System.out.println("");
			System.out.print("                      ");
		}
		System.out.println("");
		System.out.println("");
		}

	}

	public static void main(String[] args) {
			
		Scanner sc=new Scanner(System.in);
		cache_new a=new cache_new();
		System.out.println("");
		System.out.print("No. of Cache Lines: ");
		int cache_lines=sc.nextInt();
		//System.out.println("");
		System.out.print("Block Size: ");
		int block_size=sc.nextInt();
		System.out.println("");
		System.out.println("Mapping:\n1) Direct\n2) Associative\n3) N-way set Associative");
		int type_mapping=sc.nextInt();
		System.out.println("");
		if(type_mapping==1){
			a.Direct(cache_lines,block_size);
			
		}
		else if(type_mapping==2){
			a.Associative(cache_lines,block_size);
			
		}
		else{
			System.out.print("Enter N: ");
			int n=sc.nextInt();
			a.n_way_set(cache_lines,block_size,n);
		}
		System.out.println("");
		char c='y';
		while(c=='y'){
		System.out.println("1) To Read data from address\n2) To Write data to address");
		int rw_data= sc.nextInt();
		if(rw_data==1){
			System.out.print("Enter address you want to read: ");
			int adr=sc.nextInt();
			System.out.println("");

			if(type_mapping==1){
				a.direct_read(adr);
			}
			else if(type_mapping==2){
				a.associative_read(adr);
			}
			else{
				a.n_way_set_read(adr);
			}
		}
		else{
			sc.nextLine();
			System.out.println("Enter address and data: ");
			int adr=sc.nextInt();
			sc.nextLine();
			StringBuffer s=new StringBuffer(sc.nextLine());
			System.out.println("");
			if(type_mapping==1){
				a.direct_write(adr,s);
			}
			else if(type_mapping==2){
				a.associative_write(adr,s);
			}
			else{
				a.n_way_set_write(adr,s);
			}			
			
		}
		System.out.println("");
		System.out.print("Enter y to continue or n to stop: ");
		c=sc.next().charAt(0);
	}
	System.exit(0);
	}

}
