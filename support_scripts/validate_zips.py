import csv
from sets import Set

def validZipCode(zip_code):
    if isinstance(zip_code, basestring):
        zip_code = int(zip_code)
    return zip_code >= 27006 and zip_code <= 28909



with open('../src/main/resources/nc_distances_full.csv') as csvdist, open('../support_files/nc_zip_info.csv') as csvzips:
    dist_reader = csv.DictReader(csvdist)
    unique = Set()
    count = 0
    for row in dist_reader:
        unique.add(row['zip1'])
        count+=1
    print len(unique)
    print count
    print 808*807
    


