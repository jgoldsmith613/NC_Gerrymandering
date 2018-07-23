import requests, json, csv
from bs4 import BeautifulSoup

with open('../support_files/precinct_sort_statewide_at_large_contests_no_admin_precincts_20161108.txt') as txt_voting_data:
    voting_data = csv.DictReader(txt_voting_data, delimiter='\t')
    REP_COUNT = 0
    DEM_COUNT = 0
    for row in voting_data:
        if row['Contest Name'] == 'US PRESIDENT':
            if row['Choice'] == 'Hillary Clinton':
                DEM_COUNT += int(row['Total Votes'])
            if row['Choice'] == 'Donald J. Trump':
                REP_COUNT += int(row['Total Votes'])

    print 'Republican Votes: {}'.format(REP_COUNT)
    print 'Democrat Votes: {}'.format(DEM_COUNT)
    
                
