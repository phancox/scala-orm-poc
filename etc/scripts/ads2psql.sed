# Remove AquaDataStudio statement separators.
s/^\///g
# Uncomment psql specific extensions.
s/^--\\echo/\\echo/g
