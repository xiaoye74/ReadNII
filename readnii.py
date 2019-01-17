# -*- coding: utf-8 -*-

import sys,getopt
import nibabel as nib
import numpy as np


def get_files(argv):
    inputfile = ""
    outputdir = ""
    opts,args = getopt.getopt(sys.argv[1:], "hi:o:", ["version", "dir="])
    for op,value in opts:
        if op == "-i":
            print("input:",value)
            inputfile = value
        elif op == "-o":
            print("output:",value)
            outputdir = value
        elif op == "--version":
            print("version:1")
        elif op == "-h":
            print("welcome to use script!")
        elif op == "--dir":
            print("file:",value)

    img=nib.load(inputfile)
    print("load sucessfully")
    img_arr=img.get_fdata()
    img_arr=img_arr.astype(np.int16)
    filename = "%d-%d-%d.txt"%(img_arr.shape[0],img_arr.shape[1],img_arr.shape[2])
    outputfile = outputdir+"\\"+filename
    img_arr.tofile(outputfile,sep=",")
    print("write sucessfully")

if __name__ == "__main__":
    get_files(sys.argv[1:])
   
    
    
            