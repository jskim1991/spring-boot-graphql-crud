```graphql
query {
  customers {
    id,
    name
  }
}
```

```graphql
mutation {
  add(name: "Jay") {
    id
    name
  }
}
```

```graphql
mutation{
  delete(id: 1)
}
```