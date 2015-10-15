library(adegenet)
spearmanmat <- as.matrix(read.csv(file="/home/user/IdeaProjects/WWH-BioApp_resources/TreeCreator/midFiles/matrix.csv", sep=",", header=FALSE))
date <- read.csv("/home/user/IdeaProjects/WWH-BioApp_resources/TreeCreator/midFiles/dates.csv")
dates <- as.Date(date$collec.dates)
id <- date$id
res <- seqTrack(spearmanmat,x.names=id,x.dates=dates)
write.csv(res,file="/home/user/IdeaProjects/WWH-BioApp_resources/TreeCreator/midFiles/res.csv")