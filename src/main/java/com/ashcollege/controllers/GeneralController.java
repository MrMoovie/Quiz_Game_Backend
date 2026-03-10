package com.ashcollege.controllers;

import com.ashcollege.entities.*;
import com.ashcollege.responses.*;
import com.ashcollege.service.Persist;
import com.ashcollege.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.ashcollege.utils.Constants.USER_TYPE_CLIENT;
import static com.ashcollege.utils.Errors.*;

@RestController
public class GeneralController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }


    @RequestMapping ("/login")
    public BasicResponse getUser (String username, String password, int selectedType) {
        try {
            if (username != null && password != null) {
                if (selectedType == USER_TYPE_CLIENT) {
                    ClientEntity clientEntity = persist.getUserByUsernameAndPassword(username, password);
                    if (clientEntity != null) {
                        String token = GeneralUtils.hashMd5(username, password);
                        clientEntity.setToken(token);
                        persist.save(clientEntity);
                        return new LoginResponse(true, null, 1, token, clientEntity.getId(), selectedType);
                    } else {
                        return new BasicResponse(false,  ERROR_WRONG_CREDENTIALS);
                    }
                } else {
                    ProffesionalEntity proffesionalEntity = persist.getProffesionalByUsernameAndPassword(username, password);
                    if (proffesionalEntity != null) {
                        String token = GeneralUtils.hashMd5(username, password);
                        proffesionalEntity.setToken(token);
                        persist.save(proffesionalEntity);
                        return new LoginResponse(true, null, 1, token, proffesionalEntity.getId(), selectedType);
                    } else {
                        return new BasicResponse(false,  ERROR_WRONG_CREDENTIALS);
                    }

                }
            } else {
                return new BasicResponse(false, ERROR_MISSING_USERNAME_OR_PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    @RequestMapping("/signup")
    public BasicResponse addUser(int selectedType,String username,String password,String fullName,String address,String areas,int plan,String contactInfo) {
        try {
            if (username != null  && password != null && fullName != null && selectedType != 0 && contactInfo != null) {
                BasicUser userEntity = persist.getUserByUsername(username);
                if (userEntity != null) {
                    return new BasicResponse(false,ERROR_USERNAME_ALREADY_EXISTS);
                }else {
                    if (selectedType == USER_TYPE_CLIENT) {
                        if (address != null){
                            ClientEntity clientEntity = new ClientEntity();
                            clientEntity.setUsername(username);
                            clientEntity.setPassword(password);
                            clientEntity.setAddress(address);
                            clientEntity.setFullName(fullName);
                            clientEntity.setContactInfo(contactInfo);
                            String token = GeneralUtils.hashMd5(username, password);
                            clientEntity.setToken(token);
                            persist.save(clientEntity);
                            return new LoginResponse(true, null, 1, token, clientEntity.getId(), selectedType);
                        }else {
                            return new BasicResponse(false, ERROR_MISSING_VALUES);
                        }
                    }else {
                        if (areas != null && plan > 0) {
                            ProffesionalEntity proffesionalEntity = new ProffesionalEntity();
                            proffesionalEntity.setFullName(fullName);
                            proffesionalEntity.setUsername(username);
                            proffesionalEntity.setPassword(password);
                            proffesionalEntity.setAreas(areas);
                            proffesionalEntity.setPlan(plan);
                            proffesionalEntity.setContactInfo(contactInfo);
                            String token = GeneralUtils.hashMd5(username, password);
                            proffesionalEntity.setToken(token);
                            persist.save(proffesionalEntity);
                            return new LoginResponse(true, null, 1, token, proffesionalEntity.getId(), selectedType);
                        }else {
                            return new BasicResponse(false, ERROR_MISSING_VALUES);
                        }
                    }
                }
            }else {
                return new BasicResponse(false, ERROR_MISSING_VALUES);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    
    @RequestMapping("/get-user-posts")
    public BasicResponse getUserPosts(String token) {
        ClientEntity clientEntity = persist.getClientByToken(token);
        if (clientEntity != null) {
            List<PostEntity> posts = persist.getPostsByClientId(clientEntity.getId()).stream().filter(post -> !post.isDeleted()).toList();
            List<BidEntity> myProposals = persist.getProposalsByClientId(clientEntity.getId());
            return new ClientPostsResponse(true, null, posts, myProposals);
        } else {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }

    @RequestMapping("/get-all-posts")
    public BasicResponse getAllPosts(String token) {
        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
        if (proffesionalEntity != null) {
            List<PostEntity> posts = persist.getAllPost().stream().filter(post -> !post.isDeleted()).toList();
            List<BidEntity> bids = persist.getBidsByProfessionalId(proffesionalEntity.getId());
            return new ProffesionalPostsResponse(true, null, posts,bids);
        } else {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }
    @RequestMapping("/get-all-categories")
    public BasicResponse getAllCategories(String token) {
        BasicUser basicUser = persist.getUserByToken(token);
        if (basicUser != null) {
            List<CategoryEntity> categories = persist.getAllCategories().stream().filter(category -> !category.isDeleted()).toList();
            return new CategoriesResponse(true, null, categories);
        } else {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }

    @RequestMapping("/get-default-params")
    public BasicResponse getDefaultParams (String token) {
        BasicUser basicUser = persist.getUserByToken(token);
        if (basicUser != null) {
            return new DefaultParamResponse(true, null, basicUser);
        } else {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }

    @RequestMapping("/add-post")
    public BasicResponse addPost (String token, String text, String fileLink, String area,int categoryId) {
        ClientEntity clientEntity = persist.getClientByToken(token);
        CategoryEntity categoryEntity = persist.getCategoryByCategoryId(categoryId);
        if (clientEntity != null) {
            PostEntity postEntity = new PostEntity();
            postEntity.setClientEntity(clientEntity);
            postEntity.setText(text);
            postEntity.setArea(area);
            postEntity.setFileLink(fileLink);
            if (categoryEntity != null) {
                postEntity.setCategoryEntity(categoryEntity);
            }
            persist.save(postEntity);
            return new BasicResponse(true, null);
        } else {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }

    @RequestMapping("/delete-post")
    public BasicResponse deletePost (String token, int postId) {
        ClientEntity clientEntity = persist.getClientByToken(token);
        if (clientEntity != null) {
            PostEntity postEntity = persist.getPostByPostId(postId);
            if (clientEntity.getId()==postEntity.getClientEntity().getId()) {
                postEntity.setDeleted(true);
                persist.save(postEntity);
                return new BasicResponse(true, null);
            }else {
                return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
            }
        } else {
            return new BasicResponse(false,ERROR_WRONG_CREDENTIALS);
        }
    }


    @RequestMapping ("/get-all-users")
    public List<ClientEntity> getAllUsers () {
        return persist.loadList(ClientEntity.class);
    }



    @RequestMapping("/make-bid")
    public BasicResponse makeBid (String token, int postId, float proposedPrice, String description) {
        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
        if (proffesionalEntity != null) {
            PostEntity postEntity = persist.getPostByPostId(postId);
            if (postEntity != null) {
                BidEntity bidEntity = new BidEntity();
                bidEntity.setProffesionalEntity(proffesionalEntity);
                bidEntity.setPostEntity(postEntity);
                bidEntity.setProposedPrice(proposedPrice);
                bidEntity.setStatus(0);
                bidEntity.setDescription(description);
                persist.save(bidEntity);
                return new BasicResponse(true, null);
            } else {
                return new BasicResponse(false, ERROR_POST_NOT_FOUND);
            }
        } else {
            return new BasicResponse(false,ERROR_WRONG_CREDENTIALS);
        }
    }


    @RequestMapping("/my-bids")
    public BasicResponse myBids (String token) {
        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
        if (proffesionalEntity != null) {
            List<BidEntity> myBids = persist.getBidsByProfessionalId(proffesionalEntity.getId());
            return new BidsResponseModel(true, null, myBids);
        } else {
            return new BasicResponse(false,ERROR_WRONG_CREDENTIALS);
        }
    }

    @RequestMapping("/my-proposals")
    public BasicResponse myProposals (String token) {
        ClientEntity clientEntity = persist.getClientByToken(token);
        if (clientEntity != null) {
            List<BidEntity> myProposals = persist.getProposalsByClientId(clientEntity.getId()).stream().filter(proposal -> !proposal.getPostEntity().isDeleted()).toList();
            return new BidsResponseModel(true, null, myProposals);
        } else {
            return new BasicResponse(false,ERROR_WRONG_CREDENTIALS);
        }
    }

    @RequestMapping("/get-post-user")
    public BasicResponse getUserPost(String token, int id) {
        ClientEntity clientEntity = persist.getClientByToken(token);
        if (clientEntity != null) {
            PostEntity postEntity = persist.loadObject(PostEntity.class, id);
            List<BidEntity> myProposals = persist.getProposalsByClientId(clientEntity.getId());
            return new ClientPostResponse(true, null, postEntity, myProposals);
        } else {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }
    @RequestMapping("/get-post-professional")
    public BasicResponse getProfessionalPost(String token, int id) {
        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
        if (proffesionalEntity != null) {
            PostEntity postEntity = persist.loadObject(PostEntity.class, id);
            List<BidEntity> bids = persist.getBidsByProfessionalId(proffesionalEntity.getId());
            return new ProffesionalPostResponse(true, null, postEntity,bids);
        } else {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }

    @RequestMapping("/get-bid")
    public BasicResponse getBid(String token, int id) {
        BaseEntity baseEntity = persist.getUserByToken(token);
        if (baseEntity != null) {
            BidEntity bidEntity = persist.loadObject(BidEntity.class, id);
            List<MessageEntity> conversation = persist.getConversation(bidEntity.getId());
            Collections.reverse(conversation);
            return new BidResponse(true, null, bidEntity, conversation);
        } else {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }




}
