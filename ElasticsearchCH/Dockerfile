FROM elasticsearch:9.0.3

RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install  \
    --batch https://get.infini.cloud/elasticsearch/analysis-ik/9.0.3

EXPOSE 9200 9300