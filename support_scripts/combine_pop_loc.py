import csv 

def validZipCode(zip_code):
    if isinstance(zip_code, basestring):
        zip_code = int(zip_code)
    return zip_code >= 27006 and zip_code <= 28909



with open('../support_files/2010CensusPop.csv') as csvpop, open('../support_files/gaz2016zcta5centroid.csv') as csvloc, open('../support_files/nc_zip_info.csv', 'w') as output_file:
    pop_reader = csv.DictReader(csvpop)
    output = {}
    for row in pop_reader:
        if validZipCode(row['zipcode']):
            output[int(row['zipcode'])] = {'population': int(row['pop']), 'zip_code': int(row['zipcode'])}
    loc_reader = csv.DictReader(csvloc)
    for row in loc_reader:
        if validZipCode(row['zipcode']):
            result = output.get(int(row['zipcode']))
            result['lat']=row['lat']
            result['long']=row['long']
    fieldnames = ['zip_code', 'lat', 'long', 'population']
    writer = csv.DictWriter(output_file, fieldnames=fieldnames)
    writer.writeheader()
    for entry in output:
        writer.writerow(output[entry])
        

