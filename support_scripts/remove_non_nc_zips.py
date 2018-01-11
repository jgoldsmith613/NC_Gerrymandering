import csv 

def validZipCode(zip_code):
    return int(zip_code) >= 27006 and int(zip_code) <= 28909



with open('../support_files/gaz2016zcta5distancemiles.csv') as csvfile, open('../support_files/nc_distances.csv', 'w') as output:
    reader = csv.DictReader(csvfile)
    fieldnames = ['zip1', 'zip2', 'mi_to_zcta5']
    writer = csv.DictWriter(output, fieldnames=fieldnames)
    writer.writeheader()
    for row in reader:
        if validZipCode(row['zip1']) and validZipCode(row['zip2']):
            writer.writerow(row)
        

