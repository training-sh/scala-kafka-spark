# you must use multiple terminal,

Windows => Ubuntu 

```
cd /mnt/c/training
```

```
docker compose exec  broker bash
```

to list all consumers

```
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic  bookings
```

```
kafka-console-producer --broker-list localhost:9092 --topic bookings --property "parse.key=true" --property "key.separator=:"
```


```
kafka-consumer-groups --bootstrap-server localhost:9092 --list
```

to start consumer with specific group

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic bookings --group bookings-group    --property print.key=true
```


to describe a specific group 

```
kafka-consumer-groups --bootstrap-server localhost:9092 --describe  --group bookings-group
```

### Two modes

1. execute `--execute` - will reset
2. dry run `--dry-run` - will give plan, not execute or not reset

### topics, we two options

1. `--topic` - to specify a specific topics
2. `--all-topics` - to reset all topics for that consumer group


to reset offset to 0 on specific topic

```
kafka-consumer-groups --bootstrap-server localhost:9092  --group bookings-group --reset-offsets --to-earliest --execute --topic bookings
```

to reset offset to latest

```
kafka-consumer-groups --bootstrap-server localhost:9092  --group bookings-group --reset-offsets --to-latest --execute --topic bookings
```


to reset offset to specific to datetime YYYY-MM-DDTHH:mm:SS.sss

```
kafka-consumer-groups --bootstrap-server localhost:9092  --group bookings-group --reset-offsets --to-datetime 2021-06-15T11:01:01.999  --execute --topic bookings
```


and check if offset reset


```
kafka-consumer-groups --bootstrap-server localhost:9092 --describe  --group bookings-group
```


shift current ofset by n numbers

```
kafka-consumer-groups --bootstrap-server localhost:9092  --group bookings-group --reset-offsets --shift-by 10  --execute --topic bookings
```

shift by ofset negative by n numbers

```
kafka-consumer-groups --bootstrap-server localhost:9092  --group bookings-group --reset-offsets --shift-by -5   --execute --topic bookings
```


specific offset,
```
kafka-consumer-groups --bootstrap-server localhost:9092  --group bookings-group --reset-offsets --to-offset 4 --execute --topic bookings:1
```


```
kafka-consumer-groups --bootstrap-server localhost:9092  --group bookings-group --reset-offsets --to-offset 4 --execute --topic bookings:1,2
```



## Dry Run

this give plans, doesn't reset the offset... safer option, first do with dry run and then apply execute..


```
kafka-consumer-groups --bootstrap-server localhost:9092  --group bookings-group --reset-offsets --shift-by -5   --dry-run --topic bookings
```

```
kafka-consumer-groups --bootstrap-server localhost:9092  --group bookings-group --reset-offsets --to-datetime 2021-06-15T07:01:01.999  --dry-run --topic bookings
```
